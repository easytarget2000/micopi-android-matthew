package eu.ezytarget.micopi.batch

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.ViewModel

class BatchViewModel : ViewModel() {

    var contactWrappers: Array<ContactHashWrapper> = emptyArray()
        set(value) {
            field = value
            setContactWrappersLiveData()
        }
    var currentContactWrapper: ContactHashWrapper? = null
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
    var generateAndAssignImagesCallback: ((Array<ContactHashWrapper>) -> Unit)? = null
    private val contactWrapperViewModelsLiveData: MutableLiveData<List<BatchContactViewModel>> =
        MutableLiveData()

    fun setupContactViewModels(
        viewModelsOwner: LifecycleOwner,
        viewModelsObserver: Observer<List<BatchContactViewModel>>
    ) {
        contactWrapperViewModelsLiveData.observe(viewModelsOwner, viewModelsObserver)
    }

    fun onGenerateButtonClick(view: View) {
        generateAndAssignImages()
    }

    private fun setContactWrappersLiveData() {
        contactWrapperViewModelsLiveData.value = contactWrappers.map { contactHashWrapper ->
            val state: BatchContactState = when {
                currentContactWrapper == contactHashWrapper -> BatchContactState.PROCESSING
                failedContactWrappers.contains(contactHashWrapper) -> BatchContactState.FAILED
                finishedContactWrappers.contains(contactHashWrapper) -> BatchContactState.DONE
                else -> BatchContactState.UNTOUCHED
            }

            BatchContactViewModel(contactHashWrapper, state)
        }
    }

    private fun generateAndAssignImages() {
        generateAndAssignImagesCallback?.invoke(contactWrappers)
    }

}