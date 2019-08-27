package eu.ezytarget.micopi.common.data

import java.io.Serializable

data class ContactHashWrapper(val contact: Contact): Serializable {

    private var hashModifier = 0

    override fun hashCode(): Int {
        return super.hashCode() + hashModifier
    }

    fun initials(numberOfInitials: Int): String {
        if (numberOfInitials < 1) {
            return ""
        }

        return contact.name.first().toString()
    }

    /*
    Technically not required, but we don't want to violate the general contract
    for Object.hashCode(), do we?
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (javaClass != other?.javaClass) {
            return false
        }

        other as ContactHashWrapper

        if (contact != other.contact) {
            return false
        }

        if (hashModifier != other.hashModifier) {
            return false
        }

        return true
    }

    fun increaseHashModifier() {
        hashModifier++
    }

    fun decreaseHashModifier() {
        hashModifier--
    }

}