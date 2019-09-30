package eu.ezytarget.micopi.contact_preview

import com.google.firebase.analytics.FirebaseAnalytics

class ContactPreviewTracker {

    fun handleNextImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
        
    }

    fun handlePreviousImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
    }

    fun handleSaveImageToDeviceButtonClicked(firebaseInstance: FirebaseAnalytics) {
    }

    fun handleShareImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
    }

    fun handleAssignImageButtonClicked(firebaseInstance: FirebaseAnalytics) {
    }

    companion object {
        private const val NEXT_IMAGE_BUTTON_CLICKED_EVENT = "NEXT_IMAGE_BUTTON_CLICKED"
        private const val PREVIOUS_IMAGE_BUTTON_CLICKED_EVENT = "PREVIOUS_IMAGE_BUTTON_CLICKED"
        private const val SAVE_IMAGE_BUTTON_CLICKED = "SAVE_IMAGE_BUTTON_CLICKED"
        private const val SHARE_IMAGE_BUTTON_CLICKED = "SHARE_IMAGE_BUTTON_CLICKED"
        private const val ASSIGN_IMAGE_BUTTON_CLICKED = "ASSIGN_IMAGE_BUTTON_CLICKED"
    }
}