package eu.ezytarget.micopi.main_menu

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class MainMenuTracker {

    fun handleContactPickerButtonClicked(firebaseInstance: FirebaseAnalytics) {
        firebaseInstance.logEvent(CONTACT_PICKER_BUTTON_CLICKED_EVENT, null)
    }

    companion object {
        private const val CONTACT_PICKER_BUTTON_CLICKED_EVENT = "CONTACT_PICKER_BUTTON_CLICKED"
    }
}