package eu.ezytarget.micopi.contact_preview

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.ContactDatabaseImageWriter
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine
import eu.ezytarget.micopi.common.extensions.activity
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.common.permissions.WriteContactsPermissionManager
import eu.ezytarget.micopi.common.ui.ViewModel

class ContactPreviewViewModel : ViewModel() {

    var contentResolver: ContentResolver
        get() = databaseImageWriter.contentResolver
        set(value) {
            databaseImageWriter.contentResolver = value
        }
    var contactHashWrapper: ContactHashWrapper?
        get() = contactWrapperLiveData.value
        set(value) {
            contactWrapperLiveData.value = value
            generateImage()
        }
    var imageEngine: ContactImageEngine = ContactImageEngine()
    var storageImageWriter: StorageImageWriter = StorageImageWriter()
    var sharingCache: SharingCache = SharingCache()
    var storagePermissionManager: StoragePermissionManager = StoragePermissionManager()
    var databaseImageWriter: ContactDatabaseImageWriter = ContactDatabaseImageWriter()
    var contactPermissionManager: PermissionManager = WriteContactsPermissionManager()
    var listener: ContactPreviewViewModelListener? = null
    val generatedDrawable: MutableLiveData<Drawable?> = MutableLiveData()
    val contactName: LiveData<String>
        get() {
            return Transformations.map(contactWrapperLiveData) { contactWrapper ->
                contactWrapper?.contact?.displayName ?: ""
            }
        }
    val interactionEnabled: LiveData<Boolean>
        get() {
            return Transformations.map(contactWrapperLiveData) {
                if (generatedDrawable.value == null) {
                    return@map false
                } else {
                    return@map !isBusy
                }
            }
        }
    var tracker: ContactPreviewTracker = ContactPreviewTracker()
    lateinit var firebaseInstance: FirebaseAnalytics
    private var contactWrapperLiveData: MutableLiveData<ContactHashWrapper> = MutableLiveData()
    private var isBusy = false
    private val genericErrorMessage: String by lazy {
        getStringFromResourcesOrFallback(R.string.genericErrorMessage)
    }

    /*
    UI Input
     */

    fun handleNextImageButtonClicked(view: View) {
        generateNextImage()
        tracker.handleNextImageButtonClicked()
    }

    fun handlePreviousImageButtonClicked(view: View) {
        generatePreviousImage()
        tracker.handlePreviousImageButtonClicked()
    }

    fun handleSaveImageToDeviceButtonClicked(view: View) {
        val activity = view.activity!!
        validatePermissionsAndStoreImageToDevice(activity)
        tracker.handleSaveImageToDeviceButtonClicked()
    }

    fun handleShareImageButtonClicked(view: View) {
        shareImage(view.context)
        tracker.handleShareImageButtonClicked()
    }

    fun handleAssignImageButtonClicked(view: View) {
        val activity = view.activity!!
        validatePermissionsAndAssignImage(activity)
        tracker.handleAssignImageButtonClicked()
    }

    /*
    Permissions Callback
     */

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val permissionManagers = arrayOf(contactPermissionManager, storagePermissionManager)
        permissionManagers.forEach {
            it.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    /*
    Implementations
     */

    private fun generateImage() {
        if (isBusy) {
            return
        }

        isBusy = true

        imageEngine.populateColorProvider(resources ?: return)

        val generatedBitmap = imageEngine.generateBitmap(contactHashWrapper ?: return)
        handleGeneratedBitmap(generatedBitmap)
    }

    private fun handleGeneratedBitmap(bitmap: Bitmap?) {
        generatedDrawable.value = if (bitmap == null) {
            null
        } else {
            BitmapDrawable(resources, bitmap)
        }

        isBusy = false
    }

    private fun generateNextImage() {
        contactHashWrapper?.increaseHashModifier()
        generateImage()
    }

    private fun generatePreviousImage() {
        contactHashWrapper?.decreaseHashModifier()
        generateImage()
    }

    private fun validatePermissionsAndStoreImageToDevice(activity: Activity) {
        validatePermissionAndPerformAction(storagePermissionManager, activity) {
            storeImageOnDevice()
        }
    }

    private fun shareImage(context: Context) {
        val drawable = generatedDrawable.value ?: return
        val bitmap = drawable.toBitmap()

        val sharingUri = sharingCache.cacheBitmap(bitmap, context) ?: return
        listener?.onImageUriSharingRequested(sharingUri)
    }

    private fun validatePermissionsAndAssignImage(activity: Activity) {
        validatePermissionAndPerformAction(contactPermissionManager, activity) {
            assignImageToContact()
        }
    }

    private fun storeImageOnDevice() {
        val contact = contactHashWrapper?.contact ?: return
        val drawable = generatedDrawable.value ?: return
        val bitmap = drawable.toBitmap()

        val imageName = "${contact.displayName}${contact.hashCode()}"
        val storeImageDescription = getStringFromResourcesOrFallback(
            R.string.contactPreviewStoreImageDescription
        )

        val fullPath = storageImageWriter.saveBitmapToDevice(
            bitmap,
            imageName,
            storeImageDescription,
            contentResolver
        )

        val storeConfirmationFormat = getStringFromResourcesOrFallback(
            R.string.contactPreviewStoreConfirmationFormat
        )

        val message: String = if (fullPath != null) {
            String.format(storeConfirmationFormat, fullPath)
        } else {
            genericErrorMessage
        }
        showMessage(message)
    }

    private fun validatePermissionAndPerformAction(
        permissionManager: PermissionManager,
        activity: Activity,
        action: () -> Unit
    ) {
        if (!permissionManager.hasPermission(activity)) {
            permissionManager.requestPermission(activity) {
                val permissionGranted = it
                if (permissionGranted) {
                    action()
                } else {
                    showPermissionRequiredAction()
                }
            }
            return
        }

        action()
    }

    private fun showPermissionRequiredAction() {
        val permissionRequiredMessage = getStringFromResourcesOrFallback(
            R.string.contactPreviewPermissionRequiredMessage
        )
        showMessage(permissionRequiredMessage)
    }

    private fun assignImageToContact() {
        val contact = contactHashWrapper?.contact ?: return
        val drawable = generatedDrawable.value ?: return
        val bitmap = drawable.toBitmap()
        val didAssign = databaseImageWriter.assignImageToContact(bitmap, contact)
        val assignConfirmationFormat = getStringFromResourcesOrFallback(
            R.string.contactPreviewAssignConfirmationFormat
        )

        val message: String = if (didAssign) {
            String.format(assignConfirmationFormat, contact.displayName)
        } else {
            tracker.handleAssignmentError()
            genericErrorMessage
        }
        showMessage(message)
    }

    private fun showMessage(message: String) {
        listener?.onMessageRequested(message)
    }
}