package eu.ezytarget.micopi.contact_preview

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine

class ContactPreviewViewModel : ViewModel() {

    lateinit var resources: Resources
    var contactHashWrapper: ContactHashWrapper?
        get() = contactWrapperLiveData.value
        set(value) {
            contactWrapperLiveData.value = value
            generateImage()
        }
    var imageEngine: ContactImageEngine = ContactImageEngine()
    val generatedDrawable: MutableLiveData<Drawable?> = MutableLiveData()
    val contactName: LiveData<String>
        get() {
            return Transformations.map(contactWrapperLiveData) { contactWrapper ->
                contactWrapper?.contact?.name ?: ""
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

    fun handleNextImageButtonClicked(view: View) {
        generateNextImage()
    }

    fun handlePreviousImageButtonClicked(view: View) {
        generatePreviousImage()
    }

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
}