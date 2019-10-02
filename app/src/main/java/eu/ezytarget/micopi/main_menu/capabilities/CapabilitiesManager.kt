package eu.ezytarget.micopi.main_menu.capabilities

import android.content.Context

class CapabilitiesManager {

    var purchaseManager: PurchaseManager = PurchaseManager()
    var listener: CapabilitiesManagerListener? = null
    var didPurchasePlusFeatures: Boolean? = null
        private set

    fun setup(context: Context) {
        purchaseManager.startConnectionAndQueryPurchases(context) { plusProduct, errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                listener?.onCapabilitiesManagerFailedToConnect(errorMessage)
                return@startConnectionAndQueryPurchases
            } else if (plusProduct == null) {
                listener?.onCapabilitiesManagerFailedToConnect()
            } else {
                listener?.onCapabilitiesManagerLoadedAvailableProduct(plusProduct)
            }
        }
    }
}