package eu.ezytarget.micopi.main_menu.capabilities.purchase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED
import com.android.billingclient.api.BillingClient.SkuType.INAPP
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.BuildConfig

class PurchaseManager(
    private val skuDetailsConverter: SkuDetailsConverter = SkuDetailsConverter(),
    private val tracker: PurchaseTracker = PurchaseTracker()
) {

    var listener: PurchaseManagerListener? = null
    private var startedBillingFlow = false
    private lateinit var billingClient: BillingClient
    private lateinit var plusSkuDetails: SkuDetails

    fun setupTracker(firebaseInstance: FirebaseAnalytics) {
        tracker.firebaseAnalytics = firebaseInstance
    }

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
                    val errorMessage = billingResult.debugMessage
                    Log.e(tag, "onBillingSetupFinished(): $errorMessage")
                    listener?.onPurchaseManagerFailedToConnect(errorMessage)
                    tracker.handleBillingClientConnectionFailed(errorMessage)
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
        startedBillingFlow = true
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(plusSkuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams)

        tracker.handlePlusBillingFlowLaunch()
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
            handleBillingCancel()
            return
        }

        if (billingResult.responseCode != OK) {
            handleBillingNotOK(billingResult)
            return
        }

        val purchaseToken = purchases?.first()?.purchaseToken

        if (purchaseToken == null) {
            queryAvailableProducts()
            startedBillingFlow = false
        } else {
            if (startedBillingFlow) {
                acknowledgePurchase(purchaseToken)
            } else {
                handleBillingComplete()
            }
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
            when {
                billingResult.responseCode == USER_CANCELED -> handleBillingCancel()
                billingResult.responseCode != OK -> handleBillingNotOK(billingResult)
                else -> handleBillingComplete()
            }
        }
    }

    private fun handleBillingCancel() {
        startedBillingFlow = false
        tracker.handlePlusBillingFlowUserCancel()
    }

    private fun handleBillingNotOK(billingResult: BillingResult) {
        startedBillingFlow = false
        Log.e(tag, "handleBillingNotOK(): ${billingResult.debugMessage}")
        queryAvailableProducts()
    }

    private fun handleBillingComplete() {
        listener?.onPurchaseManagerPurchasedPlusProduct(startedBillingFlow)
        startedBillingFlow = false
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