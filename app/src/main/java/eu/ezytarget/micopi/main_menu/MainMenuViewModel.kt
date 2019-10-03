package eu.ezytarget.micopi.main_menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.extensions.activity
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.common.ui.ViewModel
import eu.ezytarget.micopi.main_menu.capabilities.CapabilitiesManager
import eu.ezytarget.micopi.main_menu.capabilities.CapabilitiesManagerListener
import eu.ezytarget.micopi.main_menu.capabilities.InAppProduct


class MainMenuViewModel : ViewModel() {

    var selectionListener: MainMenuSelectionListener? = null
    var paymentFlowListener: PaymentFlowListener? = null
    var contactPermissionManager: PermissionManager = ReadContactsPermissionManager()
    var contactPickerResultConverter: ContactPickerResultConverter = ContactPickerResultConverter()
    var capabilitiesManager: CapabilitiesManager = CapabilitiesManager()
    var tracker: MainMenuTracker = MainMenuTracker()
    var contactPickerButtonText: MutableLiveData<String> = MutableLiveData()
        private set
    var capabilitiesCardCopy = MutableLiveData<String>("")
        private set
    var purchaseButtonText: MutableLiveData<String> = MutableLiveData()
        private set
    var purchaseButtonVisibility = MutableLiveData<Int>(View.GONE)
        private set
    var capabilitiesCardVisibility = MutableLiveData<Int>(View.VISIBLE)
        private set
    private var allowMultipleSelection = false

    init {
        capabilitiesManager.listener = object : CapabilitiesManagerListener {
            override fun onCapabilitiesManagerFailedToConnect(errorMessage: String?) {

            }

            override fun onCapabilitiesManagerLoadedPlusProduct(inAppProduct: InAppProduct) {
                showPurchaseButton(inAppProduct)
            }

            override fun onCapabilitiesManagerFoundPlusPurchase(inPaymentFlow: Boolean) {
                showPurchaseSuccess(inPaymentFlow)
            }
        }
    }

    fun setup(context: Context, firebaseInstance: FirebaseAnalytics) {
        resources = context.resources
        tracker.firebaseInstance = firebaseInstance
        setupCapabilities(context)
    }

    fun handleSelectContactButtonClicked(view: View) {
        val activity = view.activity!!
        validatePermissionsAndSelectContactPicker(activity)
        tracker.handleContactPickerButtonClicked()
    }

    fun handlePurchaseButtonClicked(view: View) {
        val activity = view.activity!!
        startPlusPurchase(activity)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        contactPermissionManager.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    fun handleContactPickerData(data: Intent?) {
        val contacts = contactPickerResultConverter.convert(data)

        if (contacts.isEmpty()) {
            return
        }

        selectionListener?.onContactSelected(contacts.first())
    }

    private fun validatePermissionsAndSelectContactPicker(activity: Activity) {
        if (!contactPermissionManager.hasPermission(activity)) {
            contactPermissionManager.requestPermission(activity) {
                val permissionGranted = it
                if (permissionGranted) {
                    selectContactPicker()
                }
            }
            return
        }
        selectContactPicker()
    }

    private fun selectContactPicker() {
        selectionListener?.onContactPickerSelected(allowMultipleSelection)
    }

    /*
    Capabilities
     */

    private fun setupCapabilities(context: Context) {
        capabilitiesManager.setup(context)
        contactPickerButtonText.value = context.getString(
            R.string.mainMenuContactPickerButtonDefaultText
        )
        capabilitiesCardCopy.value = context.getString(R.string.mainMenuCapabilitiesCardLoadingCopy)
        purchaseButtonText.value = ""
        purchaseButtonVisibility.value = View.GONE
        capabilitiesCardVisibility.value = View.VISIBLE
    }

    private fun showPurchaseButton(plusProduct: InAppProduct) {
        capabilitiesCardCopy.value = getStringFromResourcesOrFallback(
            R.string.mainMenuCapabilitiesCardPurchaseCopy
        )

        val purchaseButtonPurchaseFormat = getStringFromResourcesOrFallback(
            R.string.mainMenuPurchaseButtonFormat
        )
        purchaseButtonText.value = String.format(
            purchaseButtonPurchaseFormat,
            plusProduct.title,
            plusProduct.formattedPrice
        )

        contactPickerButtonText.value = getStringFromResourcesOrFallback(
            R.string.mainMenuContactPickerButtonDefaultText
        )
        capabilitiesCardVisibility.value = View.VISIBLE
        purchaseButtonVisibility.value = View.VISIBLE
    }

    private fun startPlusPurchase(activity: Activity) {
        capabilitiesManager.startPlusProductPurchase(activity)
    }

    private fun showPurchaseSuccess(inPaymentFlow: Boolean) {
        capabilitiesCardVisibility.value = View.GONE
        purchaseButtonVisibility.value = View.GONE
        contactPickerButtonText.value = getStringFromResourcesOrFallback(
            R.string.mainMenuContactPickerButtonPluralText
        )

        if (inPaymentFlow) {
            paymentFlowListener?.onPaymentFlowPlusProductPurchased()
            paymentFlowListener = null
        }
    }

}