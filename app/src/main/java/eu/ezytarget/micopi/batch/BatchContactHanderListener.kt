package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.Contact

interface BatchContactHandlerListener {

    fun onBatchContactError(
        failedContact: Contact,
        failedContacts: Array<Contact>,
        contacts: Array<Contact>
    )

    fun onBatchContactSuccess(
        finishedContact: Contact,
        finishedContacts: Array<Contact>,
        contacts: Array<Contact>
    )
}