package eu.ezytarget.micopi.batch

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.ViewModel

class BatchViewModel: ViewModel() {

    private var contactHashWrappers: Array<ContactHashWrapper> = emptyArray()
    set(value) {
        field = value
        setContactWrappersLiveData()
    }

    private val contactWrapperViewModelsLiveData: MutableLiveData<List<BatchContactViewModel>>
            = MutableLiveData()

    fun setup(
        contactHashWrappers: Array<ContactHashWrapper>,
        viewModelsOwner: LifecycleOwner,
        viewModelsObserver: Observer<List<BatchContactViewModel>>
    ) {
        this.contactHashWrappers = contactHashWrappers
        contactWrapperViewModelsLiveData.observe(viewModelsOwner, viewModelsObserver)
    }

    fun onGenerateButtonClick(view: View) {

    }

    private fun setContactWrappersLiveData() {
        contactWrapperViewModelsLiveData.value = contactHashWrappers.map { contactHashWrapper ->
            BatchContactViewModel(contactHashWrapper)
        }
    }

}