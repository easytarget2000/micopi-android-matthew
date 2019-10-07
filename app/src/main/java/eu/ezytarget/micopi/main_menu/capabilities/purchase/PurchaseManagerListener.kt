package eu.ezytarget.micopi.main_menu.capabilities.purchase

interface PurchaseManagerListener {
    fun onPurchaseManagerFailedToConnect(errorMessage: String?)
    fun onPurchaseManagerLoadedPlusProduct(plusProduct: InAppProduct)
    fun onPurchaseManagerPurchasedPlusProduct(inPaymentFlow: Boolean)
}