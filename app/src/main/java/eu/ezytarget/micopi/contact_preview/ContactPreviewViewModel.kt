package eu.ezytarget.micopi.contact_preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.common.ContactHashWrapper

class ContactPreviewViewModel: ViewModel() {

    lateinit var contactWrapper: ContactHashWrapper

    val contactName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


}