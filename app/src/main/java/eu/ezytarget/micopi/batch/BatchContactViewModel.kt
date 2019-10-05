package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.Contact
import eu.ezytarget.micopi.common.data.ContactHashWrapper

data class BatchContactViewModel(
    private val contactHashWrapper: ContactHashWrapper,
    private val state: BatchContactState
) {
    val displayName: String = "${contactHashWrapper.contact.displayName} ${state.name}"
    val contact: Contact = contactHashWrapper.contact

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is BatchContactViewModel) {
            return false
        }

        return other.contactHashWrapper == contactHashWrapper
                && other.state == state
    }
}