package eu.ezytarget.micopi.batch

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.*
import eu.ezytarget.micopi.R
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
    var serviceListener: BatchViewModelServiceListener? = null
    val buttonText: LiveData<String>
        get() {
            return Transformations.map(isRunningLiveData) {
                val resourceID = if (it) {
                    android.R.string.cancel
                } else {
                    R.string.batchGenerateButtonText
                }
                getStringFromResourcesOrFallback(resourceID)
            }
        }
    private val contactWrapperViewModelsLiveData: MutableLiveData<List<BatchContactViewModel>> =
        MutableLiveData()
    private val isRunningLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onResourcesSet(resources: Resources) {
        BatchContactViewModel.untouchedStateAppendix = ""
        BatchContactViewModel.processingStateAppendix = resources.getString(
            R.string.batchContactProcessingStateAppendix
        )
        BatchContactViewModel.doneStateAppendix = resources.getString(
            R.string.batchContactSuccessStateAppendix
        )
        BatchContactViewModel.failedStatateAppendix = resources.getString(
            R.string.batchContactFailedStateAppendix
        )
    }

    fun setupContactViewModels(
        viewModelsOwner: LifecycleOwner,
        viewModelsObserver: Observer<List<BatchContactViewModel>>
    ) {
        contactWrapperViewModelsLiveData.observe(viewModelsOwner, viewModelsObserver)
    }

    fun onButtonClick(view: View) {
        handleButtonClick()
    }

    fun handleServiceStarted() {
        isRunningLiveData.value = true
    }

    fun handleServiceStopped() {
        isRunningLiveData.value = false
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

    private fun handleButtonClick() {
        val isRunning = isRunningLiveData.value ?: true
        if (isRunning) {
            serviceListener?.onBatchServiceStopRequested()
        } else {
            serviceListener?.onBatchServiceStartRequested(contactWrappers)
        }
    }

}