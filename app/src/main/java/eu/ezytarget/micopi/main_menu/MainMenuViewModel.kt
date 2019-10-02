package eu.ezytarget.micopi.main_menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.extensions.activity
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.main_menu.capabilities.CapabilitiesManager
import eu.ezytarget.micopi.main_menu.capabilities.CapabilitiesManagerListener
import eu.ezytarget.micopi.main_menu.capabilities.InAppProduct


class MainMenuViewModel: ViewModel() {

    var selectionListener: MainMenuSelectionListener? = null
    var contactPermissionManager: PermissionManager = ReadContactsPermissionManager()
    var contactPickerResultConverter: ContactPickerResultConverter = ContactPickerResultConverter()
    var capabilitiesManager: CapabilitiesManager = CapabilitiesManager()
    var tracker: MainMenuTracker = MainMenuTracker()
    var capabilitiesCardCopy = MutableLiveData<String>("")
        private set
    var purchaseButtonText: MutableLiveData<String> = MutableLiveData()
        private set
    var purchaseButtonVisibility = MutableLiveData<Int>(View.GONE)
        private set
    private var capabilitiesConnectingCopy = ""
    private var capabilitiesPurchaseCopy = ""
    private var purchaseButtonPurchaseFormat = ""
    private var allowMultipleSelection = false

    init {
        capabilitiesManager.listener = object : CapabilitiesManagerListener {
            override fun onCapabilitiesManagerFailedToConnect(errorMessage: String?) {

            }

            override fun onCapabilitiesManagerLoadedAvailableProduct(inAppProduct: InAppProduct) {
                showPurchaseButton(inAppProduct)
            }
        }
    }

    fun setup(context: Context, firebaseInstance: FirebaseAnalytics) {
        tracker.firebaseInstance = firebaseInstance
        capabilitiesConnectingCopy = context.getString(R.string.mainMenuCapabilitiesCardLoadingCopy)
        capabilitiesPurchaseCopy = context.getString(R.string.mainMenuCapabilitiesCardPurchaseCopy)
        purchaseButtonPurchaseFormat = context.getString(R.string.mainMenuPurchaseButtonFormat)

        setupCapabilitiesManager(context)
    }

    fun handleSelectContactButtonClicked(view: View) {
        val activity = view.activity!!
        validatePermissionsAndSelectContactPicker(activity)
        tracker.handleContactPickerButtonClicked()
    }

    fun handlePurchaseButtonClicked(view: View) {
        val activity = view.activity!!
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

    private fun setupCapabilitiesManager(context: Context) {
        capabilitiesManager.setup(context)
        capabilitiesCardCopy.value = capabilitiesConnectingCopy
        purchaseButtonText.value = ""
        purchaseButtonVisibility.value = View.VISIBLE
    }

    private fun showPurchaseButton(plusProduct: InAppProduct) {
        capabilitiesCardCopy.value = capabilitiesPurchaseCopy

        purchaseButtonText.value = String.format(
            purchaseButtonPurchaseFormat,
            plusProduct.title,
            plusProduct.formattedPrice
        )

        purchaseButtonVisibility.value = View.VISIBLE
    }
}