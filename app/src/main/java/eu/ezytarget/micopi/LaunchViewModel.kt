package eu.ezytarget.micopi

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel

class LaunchViewModel: ViewModel() {

    var selectionListener: LaunchSelectionListener? = null
    var contactPermissionManager: ReadContactsPermissionManager = ReadContactsPermissionManager()

    fun onContactPickerButtonClicked(activity: Activity) {
        if (!contactPermissionManager.hasReadContactsPermission(activity)) {
            contactPermissionManager.requestWriteContactsPermission(activity) {
                val permissionGranted = it
                if (permissionGranted) {
                    selectionListener?.onContactPickerSelected()
                }
            }
            return
        }
        selectionListener?.onContactPickerSelected()
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
}