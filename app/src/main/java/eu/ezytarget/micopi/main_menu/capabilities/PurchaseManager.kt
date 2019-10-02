package eu.ezytarget.micopi.main_menu.capabilities

import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import eu.ezytarget.micopi.BuildConfig

class PurchaseManager {

    var skuDetailsConverter: SkuDetailsConverter = SkuDetailsConverter()
    private lateinit var billingClient: BillingClient

    fun startConnectionAndQueryPurchases(context: Context, callback: PurchaseManagerCallback) {
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
                    callback(null, billingResult.debugMessage)
                    return
                }

                queryPurchases(callback)
            }

            override fun onBillingServiceDisconnected() {
                Log.e(tag, "onBillingServiceDisconnected()")
            }
        })
    }

    fun queryPurchases(callback: PurchaseManagerCallback) {
        val skuList = ArrayList<String>()
        skuList.add(PLUS_FEATURES_PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()) { _, skuDetailsList ->
            handleSkuDetails(skuDetailsList, callback)
        }
    }

    private fun handlePurchasesBillingResult(
        billingResult: BillingResult,
        purchases: List<Purchase>?
    ) {
        Log.d(
            tag,
            "handlePurchasesBillingResult(): ${billingResult.debugMessage}," +
                    purchases.toString()
        )
    }

    private fun handleSkuDetails(
        skuDetailsList: List<SkuDetails>,
        callback: PurchaseManagerCallback
    ) {
        if (skuDetailsList.isEmpty()) {
            callback(null, null)
            return
        }

        val plusProduct = skuDetailsConverter.convertToInAppProduct(skuDetailsList.first())
        callback(plusProduct, null)
    }

    companion object {
        val tag = PurchaseManager::class.java.name
        private val verbose = BuildConfig.DEBUG
        private const val PLUS_FEATURES_PRODUCT_ID = "plus_features"
    }

}