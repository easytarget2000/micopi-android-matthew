package eu.ezytarget.micopi.contact_preview

import android.Manifest
import eu.ezytarget.micopi.common.permissions.PermissionManager

class StoragePermissionManager: PermissionManager() {

    override val permission: String
        get() =  Manifest.permission.WRITE_EXTERNAL_STORAGE
    override val requestCode: Int
        get() = REQUEST_CODE

    companion object {
        const val REQUEST_CODE = 802
    }
}