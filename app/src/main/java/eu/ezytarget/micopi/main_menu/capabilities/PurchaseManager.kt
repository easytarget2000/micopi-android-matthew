package eu.ezytarget.micopi.main_menu.capabilities

import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import eu.ezytarget.micopi.BuildConfig

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
                    Log.e(tag, "onBillingSetupFinished(): ${billingResult.responseCode}")
                    return
                }

                queryPurchases()
            }

            override fun onBillingServiceDisconnected() {
                Log.e(tag, "onBillingServiceDisconnected()")
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
        private val verbose = BuildConfig.DEBUG
        private const val PLUS_FEATURES_PRODUCT_ID = "plus_features"
    }

}