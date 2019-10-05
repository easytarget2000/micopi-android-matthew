package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.ContactHashWrapper

interface BatchContactHandlerListener {

    fun onBatchContactError(
        failedContact: ContactHashWrapper,
        failedContacts: Array<ContactHashWrapper>,
        contacts: Array<ContactHashWrapper>
    )

    fun onBatchContactSuccess(
        finishedContact: ContactHashWrapper,
        finishedContacts: Array<ContactHashWrapper>,
        contacts: Array<ContactHashWrapper>
    )
}