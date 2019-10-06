package eu.ezytarget.micopi.main_menu.capabilities

import eu.ezytarget.micopi.common.DefaultSharedUsageStorage

class CapabilitiesStorage: DefaultSharedUsageStorage() {

    var didPurchasePlusBefore: Boolean
    get() = getBoolean(DID_PURCHASE_PLUS_BEFORE_KEY, false)
    set(value) = setValueAsync(DID_PURCHASE_PLUS_BEFORE_KEY to value)

    companion object {
        private const val DID_PURCHASE_PLUS_BEFORE_KEY = "DID_PURCHASE_PLUS_BEFORE"
    }
}