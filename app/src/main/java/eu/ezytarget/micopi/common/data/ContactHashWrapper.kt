package eu.ezytarget.micopi.common.data

import java.io.Serializable

data class ContactHashWrapper(val contact: Contact): Serializable {

    private var hashModifier = 0

    fun initials(numberOfInitials: Int): String {
        if (numberOfInitials < 1) {
            return ""
        }

        return contact.name.first().toString()
    }

    fun increaseHashModifier() {
        hashModifier++
    }

    fun decreaseHashModifier() {
        hashModifier--
    }
}