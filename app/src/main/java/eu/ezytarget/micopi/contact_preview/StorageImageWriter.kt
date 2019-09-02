package eu.ezytarget.micopi.contact_preview

import android.graphics.Bitmap

class StorageImageWriter {

    fun saveBitmapToDevice(
        bitmap: Bitmap,
        fileName: String,
        fileExtension: String = DEFAULT_FILE_EXTENSION
    ) {

    }

    companion object {
        const val DEFAULT_FILE_EXTENSION = "PNG"
    }
}