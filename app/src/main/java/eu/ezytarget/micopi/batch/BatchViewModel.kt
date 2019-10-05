package eu.ezytarget.micopi.batch

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.ViewModel

class BatchViewModel: ViewModel() {

    var contactWrappers: Array<ContactHashWrapper> = emptyArray()
    set(value) {
        field = value
        setContactWrappersLiveData()
    }
    var finishedContactWrappers: Array<ContactHashWrapper> = emptyArray()
        set(value) {
            field = value
            setContactWrappersLiveData()
        }
    var failedContactWrappers: Array<ContactHashWrapper> = emptyArray()
        set(value) {
            field = value
            setContactWrappersLiveData()
        }
    private val contactWrapperViewModelsLiveData: MutableLiveData<List<BatchContactViewModel>>
            = MutableLiveData()

    fun setupContactViewModels(
        viewModelsOwner: LifecycleOwner,
        viewModelsObserver: Observer<List<BatchContactViewModel>>
    ) {
        contactWrapperViewModelsLiveData.observe(viewModelsOwner, viewModelsObserver)
    }

    fun onGenerateButtonClick(view: View) {

    }

    private fun setContactWrappersLiveData() {
        contactWrapperViewModelsLiveData.value = contactWrappers.map { contactHashWrapper ->
            val state: BatchContactState = BatchContactState.UNTOUCHED
            BatchContactViewModel(contactHashWrapper, state)
        }
    }

}