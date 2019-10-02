package eu.ezytarget.micopi.main_menu.capabilities

import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK

class PurchaseManager {

    private lateinit var billingClient: BillingClient

    fun startConnectionAndQueryPurchases(context: Context) {
        val billingClientBuilder = BillingClient.newBuilder(context)
        billingClientBuilder.enablePendingPurchases()
        billingClientBuilder.setListener { billingResult, purchases ->
            handlePurchasesBillingResult(billingResult, purchases)
        }

        billingClient = billingClientBuilder.build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode != OK) {
                    return
                }

                queryPurchases()
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun queryPurchases() {
        val skuList = ArrayList<String>()
        skuList.add(PLUS_FEATURES_PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            handleSkuBillingResult(billingResult, skuDetailsList)
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

    private fun handleSkuBillingResult(
        billingResult: BillingResult,
        skuDetailsList: List<SkuDetails>
    ) {
        Log.d(
            tag,
            "handlePurchasesBillingResult(): ${billingResult.debugMessage}," +
                    skuDetailsList.toString()
        )
    }


    companion object {
        val tag = PurchaseManager::class.java.name
        private const val PLUS_FEATURES_PRODUCT_ID = "plus_features"
    }

}