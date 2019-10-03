package eu.ezytarget.micopi.main_menu.capabilities

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper

class CapabilitiesManager {

    var purchaseManager: PurchaseManager = PurchaseManager()
    var mainLoopHandler: Handler = Handler(Looper.getMainLooper())
    var listener: CapabilitiesManagerListener? = null
//    var didPurchasePlusFeatures: Boolean? = null
//        private set

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