package eu.ezytarget.micopi.main_menu.capabilities

interface CapabilitiesManagerListener {
    fun onCapabilitiesManagerFailedToConnect(errorMessage: String? = null)
    fun onCapabilitiesManagerLoadedAvailableProduct(inAppProduct: InAppProduct)
}