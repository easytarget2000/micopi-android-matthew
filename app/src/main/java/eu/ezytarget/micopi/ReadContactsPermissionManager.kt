package eu.ezytarget.micopi

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.app.ActivityCompat




class ReadContactsPermissionManager {

    var permission = Manifest.permission.READ_CONTACTS
    var callback: PermissionManagerCallback? = null

    fun hasReadContactsPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        val writePerm = checkSelfPermission(context, permission)
        return writePerm == PackageManager.PERMISSION_GRANTED
    }

    fun requestWriteContactsPermission(activity: Activity, callback: PermissionManagerCallback?) {
        this.callback = callback
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            READ_CONTACTS_REQUEST_CODE
        )
    }

    fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        handlePermissionChange(true)
    }

    private fun handlePermissionChange(permissionGranted: Boolean) {
        callback?.invoke(permissionGranted)
        callback = null
    }

    companion object {
        private const val READ_CONTACTS_REQUEST_CODE = 800
    }
}