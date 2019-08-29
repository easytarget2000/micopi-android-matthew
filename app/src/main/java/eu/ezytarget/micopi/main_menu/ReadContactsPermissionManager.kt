package eu.ezytarget.micopi.main_menu

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.app.ActivityCompat
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.common.permissions.PermissionManagerCallback


class ReadContactsPermissionManager: PermissionManager() {

    override val permission: String
        get() = Manifest.permission.READ_CONTACTS
    override val requestCode: Int
        get() = REQUEST_CODE

    companion object {
        const val REQUEST_CODE = 800
    }
}