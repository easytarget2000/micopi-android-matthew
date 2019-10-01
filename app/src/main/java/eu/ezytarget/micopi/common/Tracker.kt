package eu.ezytarget.micopi.common

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

abstract class Tracker {

    protected fun handleEventViaFirebase(
        firebaseInstance: FirebaseAnalytics,
        eventID: String,
        eventDetails: Bundle? = null
    ) {
        firebaseInstance.logEvent(eventID, eventDetails)
    }
}