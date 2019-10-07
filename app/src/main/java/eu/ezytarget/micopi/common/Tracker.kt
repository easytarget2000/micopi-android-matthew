package eu.ezytarget.micopi.common

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

abstract class Tracker {

    var firebaseInstance: FirebaseAnalytics? = null

    protected fun handleEventViaFirebase(
        eventID: String,
        eventDetails: Bundle? = null
    ) {
        if (firebaseInstance == null) {
            Log.e(
                javaClass.name,
                "handleEventViaFirebase(): $eventID, firebaseInstance == null"
            )
            return
        }
        firebaseInstance?.logEvent(eventID, eventDetails)
    }
}