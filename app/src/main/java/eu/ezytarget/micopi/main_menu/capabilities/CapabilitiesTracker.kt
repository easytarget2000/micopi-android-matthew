package eu.ezytarget.micopi.main_menu.capabilities

import eu.ezytarget.micopi.common.Tracker

class CapabilitiesTracker : Tracker() {

    fun handlePlusAppFound() {
        handleEventViaFirebase(PLUS_APP_FOUND_EVENT)
    }

    fun handlePlusProductPurchaseStart() {
        handleEventViaFirebase(PLUS_PRODUCT_PURCHASE_START_EVENT)
    }

    companion object {
        private const val PLUS_APP_FOUND_EVENT = "plus_app_found"
        private const val PLUS_PRODUCT_PURCHASE_START_EVENT = "plus_product_purchase_start"
    }
}