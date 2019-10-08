package eu.ezytarget.micopi.common

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

abstract class Tracker {

    var firebaseAnalytics: FirebaseAnalytics? = null

    protected fun handleEventViaFirebase(
        eventID: String,
        eventDetails: Bundle? = null
    ) {
        if (firebaseAnalytics == null) {
            Log.e(
                javaClass.name,
                "handleEventViaFirebase(): $eventID, firebaseAnalytics == null"
            )
            return
        }
        firebaseAnalytics?.logEvent(eventID, eventDetails)
    }
}