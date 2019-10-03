package eu.ezytarget.micopi.batch

import android.app.IntentService
import android.content.Intent

class BatchService: IntentService(tag)  {
    
    override fun onHandleIntent(p0: Intent?) {

    }

    companion object {
        val tag = BatchService::class.java.name
    }
}