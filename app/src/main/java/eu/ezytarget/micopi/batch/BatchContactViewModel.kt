package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.ContactHashWrapper

class BatchContactViewModel(private val contactHashWrapper: ContactHashWrapper) {

    val displayName: String = contactHashWrapper.contact.displayName

}