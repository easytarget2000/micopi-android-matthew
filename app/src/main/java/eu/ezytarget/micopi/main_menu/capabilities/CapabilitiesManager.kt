package eu.ezytarget.micopi.main_menu.capabilities

import android.content.Context

class CapabilitiesManager {

    var purchaseManager: PurchaseManager = PurchaseManager()
    var didPurchasePlusFeatures: Boolean? = null
        private set

    fun setup(context: Context) {
        purchaseManager.startConnectionAndQueryPurchases(context)
    }
}