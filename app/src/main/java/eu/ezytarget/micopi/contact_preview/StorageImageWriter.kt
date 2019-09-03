package eu.ezytarget.micopi.contact_preview

import android.content.ContentResolver
import android.graphics.Bitmap
import android.provider.MediaStore


class StorageImageWriter {

    var imageDescription = "Generated with Micopi"

    fun saveBitmapToDevice(bitmap: Bitmap, imageName: String, contentResolver: ContentResolver): String? {
        return MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            imageName,
            imageDescription
        )
    }

}
