package eu.ezytarget.micopi.batch.service

import android.content.ContentResolver
import android.content.res.Resources
import eu.ezytarget.micopi.common.data.ContactDatabaseImageWriter
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine

class BatchContactHandler {

    var engine: ContactImageEngine = ContactImageEngine()
    var contactDatabaseImageWriter: ContactDatabaseImageWriter = ContactDatabaseImageWriter()
    var stopped = true

    fun setup(contentResolver: ContentResolver) {
        contactDatabaseImageWriter.contentResolver = contentResolver
    }

    fun generateAndAssign(
        contactWrappers: Array<ContactHashWrapper>,
        resources: Resources?,
        listener: BatchContactHandlerListener? = null
    ) {
        stopped = false

        val failedContactWrappers = ArrayList<ContactHashWrapper>()
        val finishedContactWrappers = ArrayList<ContactHashWrapper>()

        if (resources != null) {
            engine.populateColorProvider(resources)
        }

        contactWrappers.forEach { contactWrapper ->
            generateAndAssign(
                contactWrapper,
                contactWrappers,
                finishedContactWrappers,
                failedContactWrappers,
                listener
            )
        }
    }

    fun stopGenerateAndAssign() {
        stopped = true
    }

    private fun generateAndAssign(
        contactWrapper: ContactHashWrapper,
        contactWrappers: Array<ContactHashWrapper>,
        finishedContactWrappers: ArrayList<ContactHashWrapper>,
        failedContactWrappers: ArrayList<ContactHashWrapper>,
        listener: BatchContactHandlerListener?
    ) {
        if (stopped) {
            return
        }

        listener?.onBatchContactProcessingStarted(contactWrapper, contactWrappers)

        val generatedBitmap = engine.generateBitmap(contactWrapper)

        if (stopped) {
            return
        }

        val didAssignImage = contactDatabaseImageWriter.assignImageToContact(
            generatedBitmap,
            contactWrapper.contact
        )

        if (didAssignImage) {
            finishedContactWrappers.add(contactWrapper)
            listener?.onBatchContactSuccess(
                contactWrapper,
                finishedContactWrappers.toTypedArray(),
                contactWrappers
            )
        } else {
            failedContactWrappers.add(contactWrapper)
            listener?.onBatchContactError(
                contactWrapper,
                failedContactWrappers.toTypedArray(),
                contactWrappers
            )
        }

        if (contactWrappers.last() === contactWrapper) {
            listener?.onBatchFinish(
                finishedContactWrappers.toTypedArray(),
                failedContactWrappers.toTypedArray(),
                contactWrappers
            )
        }
    }
}