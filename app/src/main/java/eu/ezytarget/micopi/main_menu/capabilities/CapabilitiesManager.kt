package eu.ezytarget.micopi.main_menu.capabilities

import android.app.Activity
import android.content.Context

class CapabilitiesManager(
    private val purchaseManager: PurchaseManager = PurchaseManager(),
    private val storage: CapabilitiesStorage = CapabilitiesStorage(),
    private val plusAppDetector: PlusAppDetector = PlusAppDetector()
) {
    var listener: CapabilitiesManagerListener? = null
    var hasPlusProduct = false
        private set(value) {
            field = value
            storage.didPurchasePlusBefore = value
        }
    var hasPlusApp = false
        private set

    fun getCapabilities(context: Context) {
        hasPlusApp = plusAppDetector.search(context.packageManager)
        if (hasPlusApp) {
            listener?.onCapabilitiesManagerFoundPlusApp()
        }

        storage.setup(context)
        if (storage.didPurchasePlusBefore) {
            listener?.onCapabilitiesManagerFoundPlusPurchase(false)
        }

        purchaseManager.listener = object : PurchaseManagerListener {
            override fun onPurchaseManagerFailedToConnect(errorMessage: String?) {
                listener?.onCapabilitiesManagerFailedToConnect(errorMessage)
                hasPlusProduct = false
            }

            override fun onPurchaseManagerLoadedPlusProduct(plusProduct: InAppProduct) {
                listener?.onCapabilitiesManagerLoadedPlusProduct(plusProduct)
            }

            override fun onPurchaseManagerPurchasedPlusProduct(inPaymentFlow: Boolean) {
                listener?.onCapabilitiesManagerFoundPlusPurchase(inPaymentFlow)
                hasPlusProduct = true
            }

        }
        purchaseManager.startConnectionAndQueryData(context)
    }

    fun startPlusProductPurchase(activity: Activity) {
        purchaseManager.startPlusPurchase(activity)
    }
}