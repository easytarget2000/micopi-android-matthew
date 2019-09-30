package eu.ezytarget.micopi.main_menu

import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.common.Tracker

class MainMenuTracker: Tracker() {

    fun handleContactPickerButtonClicked(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, CONTACT_PICKER_BUTTON_CLICKED_EVENT)
    }

    companion object {
        private const val CONTACT_PICKER_BUTTON_CLICKED_EVENT = "CONTACT_PICKER_BUTTON_CLICKED"
    }
}