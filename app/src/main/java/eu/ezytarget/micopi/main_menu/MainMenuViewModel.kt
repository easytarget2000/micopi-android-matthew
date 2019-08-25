package eu.ezytarget.micopi.main_menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

class MainMenuViewModel: ViewModel() {

    var contactPermissionManager: ReadContactsPermissionManager = ReadContactsPermissionManager()
    var contactPickerResultConverter: ContactPickerResultConverter = ContactPickerResultConverter()
    private var allowMultipleSelection = true
    private var selectionListenerReference: WeakReference<MainMenuSelectionListener?>? = null
    var selectionListener: MainMenuSelectionListener?
        get() = selectionListenerReference?.get()
        set(value) {
            selectionListenerReference = WeakReference(value)
        }

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

        selectionListener?.onContactSelected(contacts.first())
    }

    private fun selectContactPicker() {
        selectionListener?.onContactPickerSelected(allowMultipleSelection)
    }
}