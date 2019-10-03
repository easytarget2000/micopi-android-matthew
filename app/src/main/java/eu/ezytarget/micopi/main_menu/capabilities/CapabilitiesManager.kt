package eu.ezytarget.micopi.main_menu.capabilities

import android.app.Activity
import android.content.Context

class CapabilitiesManager {

    var purchaseManager: PurchaseManager = PurchaseManager()
    var listener: CapabilitiesManagerListener? = null
    var hasPlusProduct = false
        private set

    fun setup(context: Context) {
        purchaseManager.listener = object : PurchaseManagerListener {
            override fun onPurchaseManagerFailedToConnect(errorMessage: String?) {
                listener?.onCapabilitiesManagerFailedToConnect(errorMessage)
                hasPlusProduct = false
            }

            override fun onPurchaseManagerLoadedPlusProduct(plusProduct: InAppProduct) {
                listener?.onCapabilitiesManagerLoadedPlusProduct(plusProduct)
            }

            override fun onPurchaseManagerPurchasedPlusProduct(inPaymentFlow: Boolean) {
                listener?.onCapabilitiesManagerFoundPlusPurchase(inPaymentFlow)
                hasPlusProduct = true
            }

        }
        purchaseManager.startConnectionAndQueryData(context)
    }

    fun startPlusProductPurchase(activity: Activity) {
        purchaseManager.startPlusPurchase(activity)
    }
}