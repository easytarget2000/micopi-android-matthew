package eu.ezytarget.micopi.common.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class PermissionManager {

    abstract val permission: String
    abstract val requestCode: Int
    var callback: PermissionManagerCallback? = null

    fun hasPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        val writePerm = ContextCompat.checkSelfPermission(context, permission)
        return writePerm == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, callback: PermissionManagerCallback?) {
        this.callback = callback
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            requestCode
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

}