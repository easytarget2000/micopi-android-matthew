package eu.ezytarget.micopi

import eu.ezytarget.micopi.common.data.Contact
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import org.junit.Test

class ContactHashWrapperTests {

    @Test
    fun hashValues_AreCorrect() {
        val contact1 = Contact("0", "A")
        val contactHashWrapper1 = ContactHashWrapper(contact1)
        val contact2 = Contact("0", "A")
        val contactHashWrapper2 = ContactHashWrapper(contact2)
        val contact3 = Contact("1", "A")
        val contactHashWrapper3 = ContactHashWrapper(contact3)

        assert(contactHashWrapper1.hashCode() == contactHashWrapper2.hashCode())
        assert(contactHashWrapper1.hashCode() != contactHashWrapper3.hashCode())

        contactHashWrapper1.increaseHashModifier()
        assert(contactHashWrapper1.hashCode() != contactHashWrapper2.hashCode())

        contactHashWrapper1.decreaseHashModifier()
        assert(contactHashWrapper1.hashCode() == contactHashWrapper2.hashCode())
    }
}