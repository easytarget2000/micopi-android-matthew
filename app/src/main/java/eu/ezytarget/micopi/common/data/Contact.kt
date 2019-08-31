package eu.ezytarget.micopi.common.data

import java.io.Serializable

data class Contact(
    val entityID: String,
    val displayName: String
): Serializable {

    override fun hashCode(): Int {
        return entityID.hashCode() + displayName.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as Contact

        if (entityID != other.entityID) {
            return false
        }
        if (displayName != other.displayName) {
            return false
        }

        return true
    }

}