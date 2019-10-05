package eu.ezytarget.micopi.batch

import android.view.View
import androidx.lifecycle.MutableLiveData
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.ViewModel

class BatchViewModel: ViewModel() {

    val contactWrappersLiveData: MutableLiveData<List<ContactHashWrapper>> = MutableLiveData()
    val numberOfContacts: Int
    get() = contactWrappersLiveData.value?.size ?: 0

    fun onGenerateButtonClick(view: View) {

    }

}