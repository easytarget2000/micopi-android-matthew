package eu.ezytarget.micopi.main_menu.capabilities

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED
import com.android.billingclient.api.BillingClient.SkuType.INAPP
import eu.ezytarget.micopi.BuildConfig

class PurchaseManager {

    var skuDetailsConverter: SkuDetailsConverter = SkuDetailsConverter()
    var listener: PurchaseManagerListener? = null
    private lateinit var billingClient: BillingClient
    private lateinit var plusSkuDetails: SkuDetails

    fun startConnectionAndQueryData(context: Context) {
        val billingClientBuilder = BillingClient.newBuilder(context)
        billingClientBuilder.enablePendingPurchases()
        billingClientBuilder.setListener { billingResult, purchases ->
            handlePurchasesBillingResult(billingResult, purchases)
        }

        billingClient = billingClientBuilder.build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode != OK) {
                    Log.e(tag, "onBillingSetupFinished(): ${billingResult.debugMessage}")
                    listener?.onPurchaseManagerFailedToConnect(billingResult.debugMessage)
                    return
                }

                getCachedPurchasesAndAvailableProductsIfNeeded()
            }

            override fun onBillingServiceDisconnected() {
                Log.e(tag, "onBillingServiceDisconnected()")
            }
        })
    }

    fun startPlusPurchase(activity: Activity) {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(plusSkuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams)

        if (verbose) {
            Log.d(tag, "startPlusPurchase(): responseCode: $responseCode")
        }
    }

    private fun getCachedPurchasesAndAvailableProductsIfNeeded() {
        val purchasesResult = billingClient.queryPurchases(INAPP)
        handlePurchasesBillingResult(purchasesResult.billingResult, purchasesResult.purchasesList)
    }

    private fun handlePurchasesBillingResult(
        billingResult: BillingResult,
        purchases: List<Purchase>?
    ) {
        if (verbose) {
            Log.d(
                tag,
                "handlePurchasesBillingResult(): ${billingResult.debugMessage}," +
                        purchases.toString()
            )
        }

        if (billingResult.responseCode == USER_CANCELED) {
            return
        }

        if (billingResult.responseCode != OK) {
            Log.e(tag, "handlePurchasesBillingResult(): ${billingResult.debugMessage}")
            queryAvailableProducts()
            return
        }

        val purchasedPlus = purchases != null && purchases.isNotEmpty()

        if (purchasedPlus) {
            listener?.onPurchaseManagerPurchasedPlusProduct()
        } else {
            queryAvailableProducts()
        }
    }

    private fun queryAvailableProducts() {
        val skuList = ArrayList<String>()
        skuList.add(PLUS_FEATURES_PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(INAPP)

        billingClient.querySkuDetailsAsync(params.build()) { _, skuDetailsList ->
            handleSkuDetails(skuDetailsList)
        }
    }

    private fun handleSkuDetails(skuDetailsList: List<SkuDetails>) {
        if (skuDetailsList.isEmpty()) {
            return
        }

        plusSkuDetails = skuDetailsList.first()
        val plusProduct = skuDetailsConverter.convertToInAppProduct(plusSkuDetails)
        listener?.onPurchaseManagerLoadedPlusProduct(plusProduct)
    }

    companion object {
        val tag = PurchaseManager::class.java.name
        private val verbose = BuildConfig.DEBUG
        private const val PLUS_FEATURES_PRODUCT_ID = "plus_features"
    }

}