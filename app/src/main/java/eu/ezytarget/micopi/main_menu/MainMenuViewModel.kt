package eu.ezytarget.micopi.main_menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel

class MainMenuViewModel: ViewModel() {

    var selectionListener: MainMenuSelectionListener? = null
    var contactPermissionManager: ReadContactsPermissionManager = ReadContactsPermissionManager()
    var contactPickerResultConverter: ContactPickerResultConverter = ContactPickerResultConverter()
    private var allowMultipleSelection = true

    fun onContactPickerButtonClicked(activity: Activity) {
        if (!contactPermissionManager.hasReadContactsPermission(activity)) {
            contactPermissionManager.requestWriteContactsPermission(activity) {
                val permissionGranted = it
                if (permissionGranted) {
                    selectContactPicker()
                }
            }
            return
        }
        selectContactPicker()
    }

    fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        contactPermissionManager.onRequestPermissionsResult(
            context,
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
    }

    private fun selectContactPicker() {
        selectionListener?.onContactPickerSelected(allowMultipleSelection)
    }
}