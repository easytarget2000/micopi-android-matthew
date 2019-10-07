package eu.ezytarget.micopi.contact_preview

import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.common.Tracker

class ContactPreviewTracker: Tracker() {

    fun handleNextImageButtonClicked() {
        handleEventViaFirebase(NEXT_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handlePreviousImageButtonClicked() {
        handleEventViaFirebase(PREVIOUS_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleSaveImageToDeviceButtonClicked() {
        handleEventViaFirebase(SAVE_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleShareImageButtonClicked() {
        handleEventViaFirebase(SHARE_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleAssignImageButtonClicked() {
        handleEventViaFirebase(ASSIGN_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleAssignmentError() {
        handleEventViaFirebase(ASSIGNMENT_ERROR_EVENT)
    }

    companion object {
        private const val NEXT_IMAGE_BUTTON_CLICKED_EVENT = "next_image_button_clicked"
        private const val PREVIOUS_IMAGE_BUTTON_CLICKED_EVENT = "previous_image_button_clicked"
        private const val SAVE_IMAGE_BUTTON_CLICKED_EVENT = "save_image_button_clicked"
        private const val SHARE_IMAGE_BUTTON_CLICKED_EVENT = FirebaseAnalytics.Event.SHARE
        private const val ASSIGN_IMAGE_BUTTON_CLICKED_EVENT = "assign_image_button_clicked"
        private const val ASSIGNMENT_ERROR_EVENT = "assignment_error"
    }
}