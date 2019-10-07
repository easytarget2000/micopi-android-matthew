package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.Tracker

class BatchTracker : Tracker() {

    fun handleStartButtonClick() {
        handleEventViaFirebase(START_BUTTON_CLICKED_EVENT)
    }

    fun handleCancelButtonClick() {
        handleEventViaFirebase(CANCEL_BUTTON_CLICKED_EVENT)
    }

    companion object {
        private const val START_BUTTON_CLICKED_EVENT = "batch_start_button_clicked"
        private const val CANCEL_BUTTON_CLICKED_EVENT = "cancel_button_clicked"
    }
}