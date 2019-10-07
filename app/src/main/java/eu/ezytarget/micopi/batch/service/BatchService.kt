package eu.ezytarget.micopi.batch.service

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

    override fun onDestroy() {
        super.onDestroy()
        stopBatchHandler()
    }

    private fun stopBatchHandler() {
        batchContactHandler.stopGenerateAndAssign()
    }

    private fun broadcastProcessingStarted(
        currentContactWrapper: ContactHashWrapper,
        contactWrappers: Array<ContactHashWrapper>
    ) {
        val startBroadcast = Intent(CONTACT_START_ACTION)
        startBroadcast.putExtra(CURRENT_CONTACT_WRAPPER_EXTRA_KEY, currentContactWrapper)
        startBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contactWrappers)
        sendBroadcast(startBroadcast)
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
        val errorBroadcast = Intent(CONTACT_ERROR_ACTION)
        errorBroadcast.putExtra(CURRENT_CONTACT_WRAPPER_EXTRA_KEY, failedContactWrapper)
        errorBroadcast.putExtra(FAILED_CONTACT_WRAPPERS_EXTRA_KEY, failedContactWrappers)
        errorBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contactWrappers)
        sendBroadcast(errorBroadcast)
    }

    private fun broadcastFinish(
        finishedContactWrappers: Array<ContactHashWrapper>,
        failedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    ) {
        val finishBroadcast = Intent(FINISH_ALL_ACTION)
        finishBroadcast.putExtra(FINISHED_CONTACT_WRAPPERS_EXTRA_KEY, finishedContactWrappers)
        finishBroadcast.putExtra(FAILED_CONTACT_WRAPPERS_EXTRA_KEY, failedContactWrappers)
        finishBroadcast.putExtra(CONTACT_WRAPPERS_EXTRA_KEY, contactWrappers)
        sendBroadcast(finishBroadcast)
    }

    companion object {
        const val CONTACT_WRAPPERS_EXTRA_KEY = "CONTACT_WRAPPERS"
        const val CONTACT_START_ACTION = "CONTACT_START"
        const val CONTACT_SUCCESS_ACTION = "CONTACT_SUCCESS"
        const val CONTACT_ERROR_ACTION = "CONTACT_ERROR"
        const val FINISH_ALL_ACTION = "FINISH_ALL"
        const val CURRENT_CONTACT_WRAPPER_EXTRA_KEY = "CURRENT_CONTACT_WRAPPER"
        const val FINISHED_CONTACT_WRAPPERS_EXTRA_KEY = "FINISHED_CONTACT_WRAPPERS"
        const val FAILED_CONTACT_WRAPPERS_EXTRA_KEY = "FAILED_CONTACT_WRAPPERS"
        val tag = BatchService::class.java.name
    }
}