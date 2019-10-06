package eu.ezytarget.micopi.main_menu.capabilities

interface CapabilitiesManagerListener {
    fun onCapabilitiesManagerFailedToConnect(errorMessage: String? = null)
    fun onCapabilitiesManagerLoadedPlusProduct(plusProduct: InAppProduct)
    fun onCapabilitiesManagerFoundPlusApp()
    fun onCapabilitiesManagerFoundPlusPurchase(inPaymentFlow: Boolean)
}