package eu.ezytarget.micopi.contact_preview

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import eu.ezytarget.micopi.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class SharingCache {

    var fileName = "share.png"
    var compressionFormat = Bitmap.CompressFormat.PNG
    var compressionQuality = 100

    fun cacheBitmap(bitmap: Bitmap, context: Context): Uri? {
        val subDirName = context.getString(R.string.generatedImagesExternalPathName)
        val cachePath = File(context.cacheDir, subDirName)
        cachePath.mkdirs()

        val targetFile = File("$cachePath/$fileName")
        targetFile.createNewFile()

        val stream = FileOutputStream(targetFile)

        try {
            bitmap.compress(compressionFormat, compressionQuality, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        val authority = context.getString(R.string.sharingCacheAuthority)

        val uri: Uri
        try {
            uri = FileProvider.getUriForFile(context, authority, targetFile)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return uri
    }
}