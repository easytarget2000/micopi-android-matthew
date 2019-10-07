package eu.ezytarget.micopi.contact_preview

import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.common.Tracker

class ContactPreviewTracker: Tracker() {

    fun handleNextImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, NEXT_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handlePreviousImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, PREVIOUS_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleSaveImageToDeviceButtonClicked(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, SAVE_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleShareImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, SHARE_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleAssignImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, ASSIGN_IMAGE_BUTTON_CLICKED_EVENT)
    }

    fun handleAssignmentError(firebaseInstance: FirebaseAnalytics) {
        handleEventViaFirebase(firebaseInstance, ASSIGNMENT_ERROR_EVENT)
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