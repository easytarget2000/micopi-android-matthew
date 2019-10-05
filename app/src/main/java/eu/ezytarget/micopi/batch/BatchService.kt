package eu.ezytarget.micopi.batch

import android.app.IntentService
import android.content.Intent
import eu.ezytarget.micopi.common.data.ContactHashWrapper

class BatchService : IntentService(tag) {

    var batchContactHandler: BatchContactHandler = BatchContactHandler()

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        val contactWrappers: Array<ContactHashWrapper> = intent.getSerializableExtra(
            CONTACT_WRAPPERS_EXTRA_KEY
        ) as Array<ContactHashWrapper>

        val listener = object : BatchContactHandlerListener {
            override fun onBatchContactError(
                failedContact: ContactHashWrapper,
                failedContacts: Array<ContactHashWrapper>,
                contacts: Array<ContactHashWrapper>
            ) {
                broadcastContactError(failedContact, failedContacts, contacts)
            }

            override fun onBatchContactSuccess(
                finishedContact: ContactHashWrapper,
                finishedContacts: Array<ContactHashWrapper>,
                contacts: Array<ContactHashWrapper>
            ) {
                broadcastContactSuccess(finishedContact, finishedContacts, contacts)
            }
        }

        batchContactHandler.setup(contentResolver)
        batchContactHandler.generateAndAssign(contactWrappers, resources, listener)
    }

    private fun broadcastContactError(
        failedContact: ContactHashWrapper,
        failedContacts: Array<ContactHashWrapper>,
        contacts: Array<ContactHashWrapper>
    ) {
        val successBroadcast = Intent(CONTACT_ERROR_ACTION)
        successBroadcast.putExtra(CURRENT_CONTACT_WRAPPER_EXTRA_KEY, failedContact)
        successBroadcast.putExtra(FAILED_CONTACT_WRAPPERS_EXTRA_KEY, failedContacts)
        successBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contacts)
        sendBroadcast(successBroadcast)
    }

    private fun broadcastContactSuccess(
        finishedContact: ContactHashWrapper,
        finishedContacts: Array<ContactHashWrapper>,
        contacts: Array<ContactHashWrapper>
    ) {
        val successBroadcast = Intent(CONTACT_SUCCESS_ACTION)
        successBroadcast.putExtra(CURRENT_CONTACT_WRAPPER_EXTRA_KEY, finishedContact)
        successBroadcast.putExtra(FINISHED_CONTACT_WRAPPERS_EXTRA_KEY, finishedContacts)
        successBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contacts)
        sendBroadcast(successBroadcast)
    }

    companion object {
        const val CONTACT_WRAPPERS_EXTRA_KEY = "CONTACT_WRAPPERS"
        const val CONTACT_SUCCESS_ACTION = "CONTACT_SUCCESS"
        const val CONTACT_ERROR_ACTION = "CONTACT_ERROR"
        const val CURRENT_CONTACT_WRAPPER_EXTRA_KEY = "CURRENT_CONTACT_WRAPPER"
        const val FINISHED_CONTACT_WRAPPERS_EXTRA_KEY = "FINISHED_CONTACT_WRAPPERS"
        const val FAILED_CONTACT_WRAPPERS_EXTRA_KEY = "FAILED_CONTACT_WRAPPERS"
        val tag = BatchService::class.java.name
    }
}