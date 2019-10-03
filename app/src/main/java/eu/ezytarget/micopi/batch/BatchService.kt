package eu.ezytarget.micopi.batch

import android.app.IntentService
import android.content.Intent
import eu.ezytarget.micopi.common.data.ContactHashWrapper

class BatchService: IntentService(tag)  {

    var batchContactHandler: BatchContactHandler = BatchContactHandler()

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        val contactWrappers: Array<ContactHashWrapper> = intent.getSerializableExtra(
            CONTACT_WRAPPERS_EXTRA_KEY
        ) as Array<ContactHashWrapper>

        batchContactHandler.setup(contentResolver)
        batchContactHandler.generateAndAssign(contactWrappers, resources)
    }

    companion object {
        const val CONTACT_WRAPPERS_EXTRA_KEY = "CONTACT_WRAPPERS"
        val tag = BatchService::class.java.name
    }
}