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

            override fun onBatchContactProcessingStarted(
                currentContactWrapper: ContactHashWrapper,
                contactWrappers: Array<ContactHashWrapper>
            ) {
                broadcastProcessingStarted(currentContactWrapper, contactWrappers)
            }

            override fun onBatchContactSuccess(
                finishedContactWrapper: ContactHashWrapper,
                finishedContactWrappers: Array<ContactHashWrapper>,
                contactWrappers: Array<ContactHashWrapper>
            ) {
                broadcastContactSuccess(
                    finishedContactWrapper,
                    finishedContactWrappers,
                    contactWrappers
                )
            }

            override fun onBatchContactError(
                failedContactWrapper: ContactHashWrapper,
                failedContactWrappers: Array<ContactHashWrapper>,
                contactWrappers: Array<ContactHashWrapper>
            ) {
                broadcastContactError(failedContactWrapper, failedContactWrappers, contactWrappers)
            }

            override fun onBatchFinish(
                finishedContactWrappers: Array<ContactHashWrapper>,
                failedContactWrappers: Array<ContactHashWrapper>,
                contactWrappers: Array<ContactHashWrapper>
            ) {
                broadcastFinish(finishedContactWrappers, failedContactWrappers, contactWrappers)
            }
        }

        batchContactHandler.setup(contentResolver)
        batchContactHandler.generateAndAssign(contactWrappers, resources, listener)
    }

    private fun broadcastProcessingStarted(
        currentContactWrapper: ContactHashWrapper,
        contactWrappers: Array<ContactHashWrapper>
    ) {

    }

    private fun broadcastContactSuccess(
        finishedContactWrapper: ContactHashWrapper,
        finishedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    ) {
        val successBroadcast = Intent(CONTACT_SUCCESS_ACTION)
        successBroadcast.putExtra(CURRENT_CONTACT_WRAPPER_EXTRA_KEY, finishedContactWrapper)
        successBroadcast.putExtra(FINISHED_CONTACT_WRAPPERS_EXTRA_KEY, finishedContactWrappers)
        successBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contactWrappers)
        sendBroadcast(successBroadcast)
    }

    private fun broadcastContactError(
        failedContactWrapper: ContactHashWrapper,
        failedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    ) {
        val successBroadcast = Intent(CONTACT_ERROR_ACTION)
        successBroadcast.putExtra(CURRENT_CONTACT_WRAPPER_EXTRA_KEY, failedContactWrapper)
        successBroadcast.putExtra(FAILED_CONTACT_WRAPPERS_EXTRA_KEY, failedContactWrappers)
        successBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contactWrappers)
        sendBroadcast(successBroadcast)
    }

    private fun broadcastFinish(
        finishedContactWrappers: Array<ContactHashWrapper>,
        failedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    ) {

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