package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.Contact
import eu.ezytarget.micopi.common.data.ContactDatabaseImageWriter
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine

class BatchContactHandler {

    var engine: ContactImageEngine = ContactImageEngine()
    var contactDatabaseImageWriter: ContactDatabaseImageWriter = ContactDatabaseImageWriter()

    fun generateAndAssign(
        contactWrappers: Array<ContactHashWrapper>,
        listener: BatchContactHandlerListener? = null
    ) {
        val contacts = contactWrappers.map { contactWrapper ->
            contactWrapper.contact
        }.toTypedArray()
        val failedContacts = ArrayList<Contact>()
        val finishedContacts = ArrayList<Contact>()

        contactWrappers.forEach { contactWrapper ->
            val contact = contactWrapper.contact
            val generatedBitmap = engine.generateBitmap(contactWrapper)
            val didAssignImage = contactDatabaseImageWriter.assignImageToContact(
                generatedBitmap,
                contact
            )

            if (didAssignImage) {
                finishedContacts.add(contact)
                listener?.onBatchContactSuccess(contact, finishedContacts.toTypedArray(), contacts)
            } else {
                failedContacts.add(contact)
                listener?.onBatchContactError(contact, failedContacts.toTypedArray(), contacts)
            }
        }
    }
}