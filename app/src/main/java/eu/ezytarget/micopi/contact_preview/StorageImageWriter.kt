package eu.ezytarget.micopi.contact_preview

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


class StorageImageWriter {

    var fileExtension = "PNG"
    var compressionFormat = Bitmap.CompressFormat.PNG
    var compressionQuality = 100
    var publicDirectoryType = Environment.DIRECTORY_PICTURES!!
    var publicSubDirectory = "Micopi"

    fun saveBitmapToDevice(bitmap: Bitmap, fileNameWithoutExtension: String): Boolean {
        val pictureRootDirectory = Environment.getExternalStoragePublicDirectory(
            publicDirectoryType
        )
        val directory = File("${pictureRootDirectory}/${publicSubDirectory}")
        directory.mkdirs()

        return saveBitmapToDevice(
            bitmap,
            directory,
            fileNameWithoutExtension
        )
    }

    fun saveBitmapToDevice(
        bitmap: Bitmap,
        directory: File,
        fileNameWithoutExtension: String
    ): Boolean {
        val fullFileName = "$fileNameWithoutExtension.$fileExtension"
        val file = File(directory, fullFileName)

        if (!file.exists()) {
            file.createNewFile()
        }

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(compressionFormat, compressionQuality, outputStream)
            outputStream.flush()
            outputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

}