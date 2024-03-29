package eu.ezytarget.micopi.batch

import android.content.res.Resources
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.Contact
import eu.ezytarget.micopi.common.data.ContactHashWrapper

data class BatchContactViewModel (
    private val contactHashWrapper: ContactHashWrapper,
    private val state: BatchContactState
) {
    val displayNameAndState: String
    get() {
        val stateAppendix = when (state) {
            BatchContactState.UNTOUCHED -> untouchedStateAppendix
            BatchContactState.PROCESSING -> processingStateAppendix
            BatchContactState.DONE -> doneStateAppendix
            BatchContactState.FAILED -> failedStateAppendix
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

    override fun hashCode(): Int {
        var result = contactHashWrapper.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }

    companion object {
        var untouchedStateAppendix = ""
        var processingStateAppendix = ""
        var doneStateAppendix = ""
        var failedStateAppendix = ""

        fun initStrings(resources: Resources) {
            untouchedStateAppendix = ""
            processingStateAppendix = resources.getString(
                R.string.batchContactProcessingStateAppendix
            )
            doneStateAppendix = resources.getString(
                R.string.batchContactSuccessStateAppendix
            )
            failedStateAppendix = resources.getString(
                R.string.batchContactFailedStateAppendix
            )
        }
    }
}