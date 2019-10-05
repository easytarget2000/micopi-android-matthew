package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.Contact
import eu.ezytarget.micopi.common.data.ContactHashWrapper

data class BatchContactViewModel(
    private val contactHashWrapper: ContactHashWrapper,
    private val state: BatchContactState
) {
    val displayNameAndState: String
    get() {
        val stateAppendix = when (state) {
            BatchContactState.UNTOUCHED -> untouchedStateAppendix
            BatchContactState.PROCESSING -> processingStateAppendix
            BatchContactState.DONE -> doneStateAppendix
            BatchContactState.FAILED -> failedStatateAppendix
        }
        return "${contactHashWrapper.contact.displayName} $stateAppendix"
    }

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

    companion object {
        var untouchedStateAppendix = ""
        var processingStateAppendix = ""
        var doneStateAppendix = ""
        var failedStatateAppendix = ""
    }
}