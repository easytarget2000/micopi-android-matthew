package eu.ezytarget.micopi.batch

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.ViewModel

class BatchViewModel: ViewModel() {

    private val contactWrappersLiveData: MutableLiveData<List<ContactHashWrapper>>
            = MutableLiveData()

    fun setup(
        contactHashWrappers: Array<ContactHashWrapper>,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<List<ContactHashWrapper>>
    ) {
        contactWrappersLiveData.value = contactHashWrappers.toList()
        contactWrappersLiveData.observe(lifecycleOwner, observer)
    }

    fun onGenerateButtonClick(view: View) {

    }

}