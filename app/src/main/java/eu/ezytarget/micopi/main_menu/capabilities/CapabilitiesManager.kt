package eu.ezytarget.micopi.main_menu.capabilities

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper

class CapabilitiesManager {

    var purchaseManager: PurchaseManager = PurchaseManager()
    var mainLoopHandler: Handler = Handler(Looper.getMainLooper())
    var listener: CapabilitiesManagerListener? = null
    var didPurchasePlusFeatures: Boolean? = null
        private set

    fun setup(context: Context) {
        purchaseManager.startConnectionAndQueryPurchases(context) { plusProduct, errorMessage ->

            mainLoopHandler.run {
                if (!errorMessage.isNullOrEmpty()) {
                    listener?.onCapabilitiesManagerFailedToConnect(errorMessage)
                } else if (plusProduct == null) {
                    listener?.onCapabilitiesManagerFailedToConnect()
                } else {
                    listener?.onCapabilitiesManagerLoadedAvailableProduct(plusProduct)
                }
            }

        }
    }

    fun startPlusProductPurchase(activity: Activity) {
        purchaseManager.startPlusPurchase(activity)
    }
}