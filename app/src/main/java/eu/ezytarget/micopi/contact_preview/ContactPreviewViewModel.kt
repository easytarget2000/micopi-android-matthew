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

    private var contactWrapperLiveData: MutableLiveData<ContactHashWrapper> = MutableLiveData()

    val contactName: LiveData<String>
    get() {
        return Transformations.map(contactWrapperLiveData) { contactWrapper ->
            contactWrapper?.contact?.name ?: ""
        }
    }

//    fun setContactHashWrapper(contactHashWrapper: ContactHashWrapper) {
//        contactWrapperLiveData.value = contactHashWrapper
//    }
}