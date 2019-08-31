package eu.ezytarget.micopi.common.permissions

import android.Manifest

class WriteContactsPermissionManager: PermissionManager() {

    override val permission: String
        get() = Manifest.permission.WRITE_CONTACTS
    override val requestCode: Int
        get() = REQUEST_CODE

    companion object {
        const val REQUEST_CODE = 801
    }
}