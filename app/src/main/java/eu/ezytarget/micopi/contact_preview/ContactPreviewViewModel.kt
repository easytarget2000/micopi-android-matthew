package eu.ezytarget.micopi.contact_preview

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.engine.ContactImageEngine

class ContactPreviewViewModel : ViewModel() {

    var contactHashWrapper: ContactHashWrapper?
        get() = contactWrapperLiveData.value
        set(value) {
            contactWrapperLiveData.value = value
            generateImage()
        }
    var imageEngine: ContactImageEngine = ContactImageEngine()
    val generatedBitmap: MutableLiveData<Bitmap?> = MutableLiveData()
    val contactName: LiveData<String>
        get() {
            return Transformations.map(contactWrapperLiveData) { contactWrapper ->
                contactWrapper?.contact?.name ?: ""
            }
        }
    val interactionEnabled: LiveData<Boolean>
        get() {
            return Transformations.map(contactWrapperLiveData) { contactWrapper ->
                if (contactWrapper?.contact == null) {
                    return@map false
                } else {
                    return@map !isBusy
                }
            }
        }
    private var contactWrapperLiveData: MutableLiveData<ContactHashWrapper> = MutableLiveData()
    private var isBusy = false

    private fun generateImage() {
        isBusy = true

        val contactWrappers = arrayOf(contactHashWrapper!!)
        imageEngine.generateImageAsync(contactWrappers) { _, bitmap, _, _ ->
            generatedBitmap.value = bitmap
            isBusy = false
        }
    }
}