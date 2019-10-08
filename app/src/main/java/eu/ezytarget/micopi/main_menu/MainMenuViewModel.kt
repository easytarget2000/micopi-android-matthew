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
import eu.ezytarget.micopi.main_menu.capabilities.purchase.InAppProduct


class MainMenuViewModel : ViewModel() {

    var selectionListener: MainMenuSelectionListener? = null
    var paymentFlowListener: PaymentFlowListener? = null
    var contactPermissionManager: PermissionManager = ReadContactsPermissionManager()
    var contactPickerResultConverter: ContactPickerResultConverter = ContactPickerResultConverter()
    var capabilitiesManager: CapabilitiesManager = CapabilitiesManager()
    var tracker: MainMenuTracker = MainMenuTracker()
    val contactPickerButtonText: MutableLiveData<String> = MutableLiveData()
    val capabilityViewsVisibility = MutableLiveData<Int>(View.VISIBLE)
    val capabilitiesCardCopy = MutableLiveData<String>("")
    val purchaseButtonText: MutableLiveData<String> = MutableLiveData()
    val purchaseButtonVisibility = MutableLiveData<Int>(View.GONE)
    val customerPromoVisibility = MutableLiveData<Int>(View.GONE)

    init {
        capabilitiesManager.listener = object : CapabilitiesManagerListener {
            override fun onCapabilitiesManagerFailedToConnect(errorMessage: String?) {
            }

            override fun onCapabilitiesManagerLoadedPlusProduct(inAppProduct: InAppProduct) {
                showPurchaseAvailability(inAppProduct)
            }

            override fun onCapabilitiesManagerFoundPlusApp() {
            }

            override fun onCapabilitiesManagerFoundPlusPurchase(inPaymentFlow: Boolean) {
                showPurchaseSuccess(inPaymentFlow)
            }
        }
    }

    fun setupAndGetCapabilities(context: Context, firebaseInstance: FirebaseAnalytics) {
        resources = context.resources
        tracker.firebaseAnalytics = firebaseInstance
        capabilitiesManager.setupTrackers(firebaseInstance)
        getCapabilities(context)
    }

    fun onSelectContactButtonClick(view: View) {
        val activity = view.activity!!
        validatePermissionsAndSelectContactPicker(activity)
        tracker.handleContactPickerButtonClicked()
    }

    fun onPurchaseButtonClick(view: View) {
        val activity = view.activity!!
        startPlusPurchase(activity)
    }

    fun onSendMailButtonClick(view: View) {
        selectionListener?.sendPromoMailSelected()
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

        if (contacts.size > 1) {
            selectionListener?.onContactsSelected(contacts)
        } else {
            selectionListener?.onContactSelected(contacts.first())
        }
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
        val allowMultipleSelection = capabilitiesManager.hasPlusProduct
        selectionListener?.onContactPickerSelected(allowMultipleSelection)
    }

    /*
    Capabilities
     */

    private fun getCapabilities(context: Context) {
        capabilitiesManager.getCapabilities(context)

        contactPickerButtonText.value = context.getString(
            R.string.mainMenuContactPickerButtonDefaultText
        )
        capabilitiesCardCopy.value = context.getString(R.string.mainMenuCapabilitiesCardLoadingCopy)
        purchaseButtonText.value = ""
        purchaseButtonVisibility.value = View.GONE
        capabilityViewsVisibility.value = View.VISIBLE
    }

    private fun showPurchaseAvailability(plusProduct: InAppProduct) {
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
        capabilityViewsVisibility.value = View.VISIBLE
        purchaseButtonVisibility.value = View.VISIBLE

        if (capabilitiesManager.hasPlusApp) {
            customerPromoVisibility.value = View.VISIBLE
        } else {
            customerPromoVisibility.value = View.GONE
        }
    }

    private fun startPlusPurchase(activity: Activity) {
        capabilitiesManager.startPlusProductPurchase(activity)
    }

    private fun showPurchaseSuccess(inPaymentFlow: Boolean) {
        capabilityViewsVisibility.value = View.GONE
        purchaseButtonVisibility.value = View.GONE
        contactPickerButtonText.value = getStringFromResourcesOrFallback(
            R.string.mainMenuContactPickerButtonPluralText
        )
        customerPromoVisibility.value = View.GONE

        if (inPaymentFlow) {
            paymentFlowListener?.onPaymentFlowPlusProductPurchased()
            paymentFlowListener = null
        }
    }

}