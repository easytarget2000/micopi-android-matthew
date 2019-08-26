package eu.ezytarget.micopi.main_menu

import android.content.Intent
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.MultiContactPicker
import eu.ezytarget.micopi.common.Contact
import eu.ezytarget.micopi.common.ContactHashWrapper


class ContactPickerResultConverter {

    fun convert(data: Intent?): Array<ContactHashWrapper> {
        if (data == null) {
            return emptyArray()
        }
        val results = MultiContactPicker.obtainResult(data)

        return results.map { convertResult(it) }.toTypedArray()
    }

    private fun convertResult(contactResult: ContactResult): ContactHashWrapper {
        val contact = Contact(contactResult.displayName)
        return ContactHashWrapper(contact)
    }
}