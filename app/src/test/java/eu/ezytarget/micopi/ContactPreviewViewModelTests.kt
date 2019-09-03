package eu.ezytarget.micopi

import android.Manifest
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import eu.ezytarget.micopi.common.permissions.PermissionManagerCallback
import eu.ezytarget.micopi.contact_preview.ContactPreviewViewModel
import org.junit.Assert.fail
import org.junit.Test

class ContactPreviewViewModelTests {

    @Test
    fun permissionChanges_AreHandledCorrectly() {
        val viewModel = ContactPreviewViewModel()
        val contactPermissionManager = viewModel.contactPermissionManager
        val storagePermissionManager = viewModel.storagePermissionManager
        val failCallback: PermissionManagerCallback = {
            fail()
        }
        val emptyResults = IntArray(0)

        contactPermissionManager.callback = failCallback
        storagePermissionManager.callback = failCallback

        viewModel.onRequestPermissionsResult(-1, emptyArray(), emptyResults)
        viewModel.onRequestPermissionsResult(
            contactPermissionManager.requestCode,
            emptyArray(),
            emptyResults
        )

        val deniedCallback: PermissionManagerCallback = { granted ->
            assert(!granted)
        }
        contactPermissionManager.callback = deniedCallback
        viewModel.onRequestPermissionsResult(
            contactPermissionManager.requestCode,
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            IntArray(1) {
                PERMISSION_DENIED
            }
        )

        val grantedCallback: PermissionManagerCallback = { granted ->
            assert(granted)
        }
        storagePermissionManager.callback = grantedCallback
        viewModel.onRequestPermissionsResult(
            contactPermissionManager.requestCode,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            IntArray(1) {
                PERMISSION_GRANTED
            }
        )
    }
}