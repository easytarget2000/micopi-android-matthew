package eu.ezytarget.micopi.contact_preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.common.ContactHashWrapper

class ContactPreviewViewModel : ViewModel() {

    var contactHashWrapper: ContactHashWrapper?
        get() = contactWrapperLiveData.value
        set(value) {
            contactWrapperLiveData.value = value
        }

    private var contactWrapperLiveData: MutableLiveData<ContactHashWrapper?> by lazy {
        MutableLiveData<ContactHashWrapper?>(null)
    }

    val contactName: LiveData<String> =
        Transformations.map(contactWrapperLiveData) { contactWrapper ->
            contactWrapper?.contact?.name ?: ""
        }

}