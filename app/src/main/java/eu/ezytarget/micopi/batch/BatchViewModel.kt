package eu.ezytarget.micopi.batch

import android.app.Activity
import android.content.res.Resources
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.batch.service.BatchViewModelServiceListener
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.extensions.activity
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.common.permissions.WriteContactsPermissionManager
import eu.ezytarget.micopi.common.ui.ViewModel

class BatchViewModel : ViewModel() {

    var tracker: BatchTracker = BatchTracker()
    var contactPermissionManager: PermissionManager = WriteContactsPermissionManager()
    var contactWrappers: Array<ContactHashWrapper> = emptyArray()
        set(value) {
            field = value
            setContactWrappersLiveData()
        }
    var currentContactWrapper: ContactHashWrapper? = null
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

    /*
    Activity Input
     */

    override fun onResourcesSet(resources: Resources) {
        BatchContactViewModel.initStrings(resources)
    }

    fun setupTracker(firebaseInstance: FirebaseAnalytics) {
        tracker.firebaseAnalytics = firebaseInstance
    }

    fun setupContactViewModels(
        viewModelsOwner: LifecycleOwner,
        viewModelsObserver: Observer<List<BatchContactViewModel>>
    ) {
        contactWrapperViewModelsLiveData.observe(viewModelsOwner, viewModelsObserver)
    }

    fun onButtonClick(view: View) {
        val activity = view.activity!!
        handleButtonClick(activity)
    }

    fun handleServiceStarted() {
        isRunningLiveData.value = true
    }

    fun handleServiceStopped() {
        isRunningLiveData.value = false
        currentContactWrapper = null
    }

    /*
    Permissions Callback
     */

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        contactPermissionManager.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    /*
    Implementations
     */

    private fun setContactWrappersLiveData() {
        contactWrapperViewModelsLiveData.value = contactWrappers.map { contactHashWrapper ->
            val state: BatchContactState = when {
                failedContactWrappers.contains(contactHashWrapper) -> BatchContactState.FAILED
                finishedContactWrappers.contains(contactHashWrapper) -> BatchContactState.DONE
                currentContactWrapper == contactHashWrapper -> BatchContactState.PROCESSING
                else -> BatchContactState.UNTOUCHED
            }

            BatchContactViewModel(contactHashWrapper, state)
        }
    }

    private fun handleButtonClick(activity: Activity) {
        val isRunning = isRunningLiveData.value ?: true
        if (isRunning) {
            serviceListener?.onBatchServiceStopRequested()
            tracker.handleCancelButtonClick()
        } else {
            validatePermissionsAndStartService(activity)
            tracker.handleStartButtonClick()
        }
    }

    private fun validatePermissionsAndStartService(activity: Activity) {
        validatePermissionAndPerformAction(contactPermissionManager, activity) {
            serviceListener?.onBatchServiceStartRequested(contactWrappers)
        }
    }

    companion object {
        val tag = BatchViewModel::class.java.name
    }
}