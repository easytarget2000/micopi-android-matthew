package eu.ezytarget.micopi.main_menu

import eu.ezytarget.micopi.common.Tracker

class MainMenuTracker: Tracker() {

    fun handleContactPickerButtonClicked() {
        handleEventViaFirebase(CONTACT_PICKER_BUTTON_CLICKED_EVENT)
    }

    companion object {
        private const val CONTACT_PICKER_BUTTON_CLICKED_EVENT = "contact_picker_button_clicked"
    }
}