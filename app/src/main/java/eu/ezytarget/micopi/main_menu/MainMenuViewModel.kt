package eu.ezytarget.micopi.main_menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.extensions.activity
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.main_menu.capabilities.CapabilitiesManager
import eu.ezytarget.micopi.main_menu.capabilities.InAppProduct


class MainMenuViewModel: ViewModel() {

    var selectionListener: MainMenuSelectionListener? = null
    var contactPermissionManager: PermissionManager = ReadContactsPermissionManager()
    var contactPickerResultConverter: ContactPickerResultConverter = ContactPickerResultConverter()
    var capabilitiesManager: CapabilitiesManager = CapabilitiesManager()
    var tracker: MainMenuTracker = MainMenuTracker()
    var capabilitiesCardCopy: String = ""
        private set
    var purchaseButtonText: String = ""
        private set
    var purchaseButtonVisibility = View.GONE
        private set
    private var purchaseButtonPurchaseFormat: String = ""
    private var allowMultipleSelection = false

    fun setup(context: Context, firebaseInstance: FirebaseAnalytics) {
        capabilitiesManager.setup(context)
        tracker.firebaseInstance = firebaseInstance
        capabilitiesCardCopy = context.getString(R.string.mainMenuCapabilitiesCardLoadingCopy)
        purchaseButtonPurchaseFormat = context.getString(R.string.mainMenuPurchaseButtonFormat)

        showPurchaseButton(InAppProduct("", "Test", "10000€"))
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

    private fun showPurchaseButton(inAppProduct: InAppProduct) {
        purchaseButtonText = String.format(
            purchaseButtonPurchaseFormat,
            inAppProduct.title,
            inAppProduct.formattedPrice
        )

        purchaseButtonVisibility = View.VISIBLE
    }
}