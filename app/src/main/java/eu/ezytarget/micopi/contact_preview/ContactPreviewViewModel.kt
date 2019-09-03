package eu.ezytarget.micopi.contact_preview

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.common.extensions.activity
import eu.ezytarget.micopi.common.data.ContactDatabaseImageWriter
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine
import eu.ezytarget.micopi.common.permissions.PermissionManager
import eu.ezytarget.micopi.common.permissions.WriteContactsPermissionManager

class ContactPreviewViewModel : ViewModel() {

    lateinit var resources: Resources
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
    var imageWriter: StorageImageWriter = StorageImageWriter()
    var sharingCache: SharingCache = SharingCache()
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
    private var contactWrapperLiveData: MutableLiveData<ContactHashWrapper> = MutableLiveData()
    private var isBusy = false

    /*
    UI Input
     */

    fun handleNextImageButtonClicked(view: View) {
        generateNextImage()
    }

    fun handlePreviousImageButtonClicked(view: View) {
        generatePreviousImage()
    }

    fun handleSaveImageButtonClicked(view: View) {
        storeImage()
    }

    fun handleShareImageButtonClicked(view: View) {
        shareImage(view.context)
    }

    fun handleAssignImageButtonClicked(view: View) {
        val activity = view.activity!!
        validatePermissionsAndAssignImage(activity)
    }

    /*
    Permissions Callback
     */

    fun onRequestPermissionsResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        contactPermissionManager.onRequestPermissionsResult(
            context,
            requestCode,
            permissions,
            grantResults
        )
    }

    /*
    Implementations
     */

    private fun generateImage() {
        if (isBusy) {
            return
        }

        isBusy = true

        val contactWrappers = arrayOf(contactHashWrapper!!)
        imageEngine.generateImageAsync(contactWrappers, resources) { _, bitmap, _, _ ->
            handleGeneratedBitmap(bitmap)
        }
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

    private fun storeImage() {
        val drawable = generatedDrawable.value ?: return
        val bitmap = drawable.toBitmap()
    }

    private fun shareImage(context: Context) {
        val drawable = generatedDrawable.value ?: return
        val bitmap = drawable.toBitmap()

        val sharingUri = sharingCache.cacheBitmap(bitmap, context) ?: return
        listener?.onImageUriSharingRequested(sharingUri)
    }

    private fun validatePermissionsAndAssignImage(activity: Activity) {
        if (!contactPermissionManager.hasPermission(activity)) {
            contactPermissionManager.requestPermission(activity) {
                val permissionGranted = it
                if (permissionGranted) {
                    assignImageToContact()
                }
            }
            return
        }

        assignImageToContact()
    }

    private fun assignImageToContact() {
        val contact = contactHashWrapper?.contact ?: return
        val drawable = generatedDrawable.value ?: return
        val bitmap = drawable.toBitmap()
        databaseImageWriter.assignImageToContact(bitmap, contact)
    }
}