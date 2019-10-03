package eu.ezytarget.micopi.main_menu.capabilities

import android.app.Activity
import android.content.Context

class CapabilitiesManager {

    var purchaseManager: PurchaseManager = PurchaseManager()
    var listener: CapabilitiesManagerListener? = null
    
    fun setup(context: Context) {
        purchaseManager.listener = object : PurchaseManagerListener {
            override fun onPurchaseManagerFailedToConnect(errorMessage: String?) {
                listener?.onCapabilitiesManagerFailedToConnect(errorMessage)
            }

            override fun onPurchaseManagerLoadedPlusProduct(plusProduct: InAppProduct) {
                listener?.onCapabilitiesManagerLoadedPlusProduct(plusProduct)
            }

            override fun onPurchaseManagerPurchasedPlusProduct(inPaymentFlow: Boolean) {
                listener?.onCapabilitiesManagerFoundPlusPurchase(inPaymentFlow)
            }

        }
        purchaseManager.startConnectionAndQueryData(context)
    }

    fun startPlusProductPurchase(activity: Activity) {
        purchaseManager.startPlusPurchase(activity)
    }
}