package eu.ezytarget.micopi.batch.service

import android.content.ContentResolver
import android.content.res.Resources
import eu.ezytarget.micopi.common.data.ContactDatabaseImageWriter
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine

class BatchContactHandler {

    var engine: ContactImageEngine = ContactImageEngine()
    var contactDatabaseImageWriter: ContactDatabaseImageWriter = ContactDatabaseImageWriter()

    fun setup(contentResolver: ContentResolver) {
        contactDatabaseImageWriter.contentResolver = contentResolver
    }

    fun generateAndAssign(
        contactWrappers: Array<ContactHashWrapper>,
        resources: Resources?,
        listener: BatchContactHandlerListener? = null
    ) {
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

    private fun generateAndAssign(
        contactWrapper: ContactHashWrapper,
        contactWrappers: Array<ContactHashWrapper>,
        finishedContactWrappers: ArrayList<ContactHashWrapper>,
        failedContactWrappers: ArrayList<ContactHashWrapper>,
        listener: BatchContactHandlerListener?
    ) {
        listener?.onBatchContactProcessingStarted(contactWrapper, contactWrappers)

        val generatedBitmap = engine.generateBitmap(contactWrapper)
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