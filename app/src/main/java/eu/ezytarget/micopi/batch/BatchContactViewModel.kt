package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.ContactHashWrapper

data class BatchContactViewModel(
    private val contactHashWrapper: ContactHashWrapper,
    private val state: BatchContactState
) {

    val displayName: String = "${contactHashWrapper.contact.displayName} ${state.name}"
    val contactID = contactHashWrapper.contact.entityID
}