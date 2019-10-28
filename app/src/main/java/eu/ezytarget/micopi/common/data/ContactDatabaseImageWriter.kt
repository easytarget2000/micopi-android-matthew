package eu.ezytarget.micopi.common.data

import android.content.ContentResolver
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

class ContactDatabaseImageWriter {

    lateinit var contentResolver: ContentResolver

    fun assignImageToContact(bitmap: Bitmap?, contact: Contact): Boolean {
        val rawContactUri = getContactUri(contact.entityID) ?: return false

        val thumbnailValues = ContentValues()
        thumbnailValues.put(ContactsContract.Data.RAW_CONTACT_ID, contact.entityID)
        thumbnailValues.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1)

        val downscaledImageBytes = downscaleAndCompressIntoBytes(bitmap)
        thumbnailValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, downscaledImageBytes)

        thumbnailValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        )

        val photoID = getContactPhotoID(contact.entityID)
        val hasPhoto = photoID != null
        if (hasPhoto) {
            contentResolver.update(
                ContactsContract.Data.CONTENT_URI,
                thumbnailValues,
                ContactsContract.Data._ID + "=" + photoID,
                null
            )
        } else {
            contentResolver.insert(
                ContactsContract.Data.CONTENT_URI,
                thumbnailValues
            )
        }

        return overwriteHiResPhoto(rawContactUri, bitmap)
    }

    private fun getContactUri(contactID: String): Uri? {

        val uriCursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts._ID),
            ContactsContract.RawContacts.CONTACT_ID + "=?",
            arrayOf(contactID),
            null
        )

        if (uriCursor == null) {
            Log.e(tag, "uriCursor is null.")
            return null
        }

        val rawContactUri: Uri

        if (uriCursor.moveToFirst()) {
            val contentUri = ContactsContract.RawContacts.CONTENT_URI
            val rawPath = uriCursor.getLong(0).toString()
            rawContactUri = contentUri.buildUpon().appendPath(rawPath).build()
        } else {
            uriCursor.close()
            return null
        }
        uriCursor.close()

        return rawContactUri
    }

    private fun getContactPhotoID(contactID: String): Int? {
        val photoSelection = (ContactsContract.Data.RAW_CONTACT_ID + "=="
                + contactID
                + " AND "
                + ContactsContract.RawContacts.Data.MIMETYPE + "=='"
                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'")

        val existingPhotoCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            photoSelection,
            null,
            null
        )

        var photoID: Int? = null

        if (existingPhotoCursor != null) {
            val index = existingPhotoCursor.getColumnIndex(ContactsContract.Data._ID)
            if (index > 0 && existingPhotoCursor.moveToFirst()) {
                photoID = existingPhotoCursor.getInt(index)
            }
            existingPhotoCursor.close()
        }

        return photoID
    }

    private fun overwriteHiResPhoto(
        rawContactUri: Uri,
        hiResBitmap: Bitmap?
    ): Boolean {
        val displayPhotoUri = Uri.withAppendedPath(
            rawContactUri,
            ContactsContract.Contacts.Photo.DISPLAY_PHOTO
        )
        var descriptor: AssetFileDescriptor? = null
        try {
            descriptor = contentResolver.openAssetFileDescriptor(
                displayPhotoUri,
                FILE_DESCRIPTOR_WRITE_MODE
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (descriptor == null) {
            return false
        }

        val outputStream: OutputStream
        try {
            outputStream = descriptor.createOutputStream()
            hiResBitmap?.compress(hiResImageFormat, HI_RES_IMAGE_COMPRESSION_QUALITY, outputStream)
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    private fun downscaleAndCompressIntoBytes(bitmap: Bitmap?): ByteArray {
        val downscaledImageStream = ByteArrayOutputStream()

        if (bitmap != null) {
            val downscaledBitmap = downscaleBitmap(bitmap)
            downscaledBitmap.compress(
                downscaledImageFormat,
                DOWNSCALED_IMAGE_COMPRESSION_QUALITY,
                downscaledImageStream
            )
        }

        val bytes = downscaledImageStream.toByteArray()
        try {
            downscaledImageStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bytes
    }

    private fun downscaleBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            DOWNSCALED_IMAGE_WIDTH,
            DOWNSCALED_IMAGE_HEIGHT,
            FILTER_DOWNSCALE
        )
    }

    companion object {
        val tag = ContactDatabaseImageWriter::class.java.simpleName
        private const val DOWNSCALED_IMAGE_WIDTH = 64
        private const val DOWNSCALED_IMAGE_HEIGHT = DOWNSCALED_IMAGE_WIDTH
        private const val DOWNSCALED_IMAGE_COMPRESSION_QUALITY = 100
        private const val HI_RES_IMAGE_COMPRESSION_QUALITY = 100
        private const val FILTER_DOWNSCALE = true
        private const val FILE_DESCRIPTOR_WRITE_MODE = "w"
        private val downscaledImageFormat = Bitmap.CompressFormat.JPEG
        private val hiResImageFormat = Bitmap.CompressFormat.JPEG
    }
}
