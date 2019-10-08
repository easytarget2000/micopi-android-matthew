package eu.ezytarget.micopi.common.ui

import android.app.Activity
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.permissions.PermissionManager

abstract class ViewModel : ViewModel() {
    var messageListener: ViewModelMessageListener? = null
    var resources: Resources? = null
    set(value) {
        field = value
        if (value != null) {
            onResourcesSet(value)
        }
    }

    protected fun validatePermissionAndPerformAction(
        permissionManager: PermissionManager,
        activity: Activity,
        action: () -> Unit
    ) {
        if (!permissionManager.hasPermission(activity)) {
            permissionManager.requestPermission(activity) {
                val permissionGranted = it
                if (permissionGranted) {
                    action()
                } else {
                    showPermissionRequiredAction()
                }
            }
            return
        }

        action()
    }

    protected fun getStringFromResourcesOrFallback(@StringRes resourcesID: Int): String {
        return resources?.getString(resourcesID) ?: RESOURCES_NULL_FALLBACK
    }

    protected open fun onResourcesSet(resources: Resources) {

    }

    private fun showPermissionRequiredAction() {
        val permissionRequiredMessage = getStringFromResourcesOrFallback(
            R.string.contactPreviewPermissionRequiredMessage
        )
        showMessage(permissionRequiredMessage)
    }

    protected fun showMessage(message: String) {
        messageListener?.onMessageRequested(message)
    }

    companion object {
        private const val RESOURCES_NULL_FALLBACK = "RESOURCES_NULL_FALLBACK"
    }
}