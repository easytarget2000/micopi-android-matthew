package eu.ezytarget.micopi.common.engine

import eu.ezytarget.micopi.common.data.ContactHashWrapper

class ContactImageEngine {

    private lateinit var contactHashWrappers: Array<ContactHashWrapper>
    private var stopped = false

    fun generateImageAsync(
        contactHashWrappers: Array<ContactHashWrapper>,
        callback: ContatImageEngineCallback?
    ) {
        this.contactHashWrappers = contactHashWrappers
        stopped = false

        contactHashWrappers.forEach {
            generateImage(it, callback)
        }
    }

    fun generateImage(
        contactHashWrapper: ContactHashWrapper,
        callback: ContatImageEngineCallback?
    ) {
        callback?.invoke(contactHashWrapper, null, true, true)
    }
}