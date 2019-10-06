package eu.ezytarget.micopi.common.ui

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

abstract class ViewModel : ViewModel() {
    var resources: Resources? = null
    set(value) {
        field = value
        if (value != null) {
            onResourcesSet(value)
        }
    }

    protected fun getStringFromResourcesOrFallback(@StringRes resourcesID: Int): String {
        return resources?.getString(resourcesID) ?: RESOURCES_NULL_FALLBACK
    }

    protected open fun onResourcesSet(resources: Resources) {

    }

    companion object {
        private const val RESOURCES_NULL_FALLBACK = "RESOURCES_NULL_FALLBACK"
    }
}