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
        private const val NEXT_IMAGE_BUTTON_CLICKED_EVENT = "NEXT_IMAGE_BUTTON_CLICKED"
        private const val PREVIOUS_IMAGE_BUTTON_CLICKED_EVENT = "PREVIOUS_IMAGE_BUTTON_CLICKED"
        private const val SAVE_IMAGE_BUTTON_CLICKED_EVENT = "SAVE_IMAGE_BUTTON_CLICKED"
        private const val SHARE_IMAGE_BUTTON_CLICKED_EVENT = "SHARE_IMAGE_BUTTON_CLICKED"
        private const val ASSIGN_IMAGE_BUTTON_CLICKED_EVENT = "ASSIGN_IMAGE_BUTTON_CLICKED"
        private const val ASSIGNMENT_ERROR_EVENT = "ASSIGNMENT_ERROR"
    }
}