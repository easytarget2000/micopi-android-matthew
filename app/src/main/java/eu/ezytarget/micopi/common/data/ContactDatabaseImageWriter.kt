package eu.ezytarget.micopi.common.data

import android.content.ContentResolver
import android.content.ContentUris
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
        val rawContactUri = getContactUri(contact.databaseID) ?: return false

        val values = ContentValues()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, ContentUris.parseId(rawContactUri))
        values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1)

        val downscaledImageBytes = downscaleAndCompressIntoBytes(bitmap)
        values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, downscaledImageBytes)

        values.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        )

        val photoID = getContactPhotoID(rawContactUri)
        val hasPhoto = photoID >= 0
        if (hasPhoto) {
            contentResolver.update(
                ContactsContract.Data.CONTENT_URI,
                values,
                ContactsContract.Data._ID + "=" + photoID, null
            )
        } else {
            contentResolver.insert(
                ContactsContract.Data.CONTENT_URI,
                values
            )
        }

        overwriteHiResPhoto(contentResolver, rawContactUri, bitmap)

        return true
    }

    private fun getContactUri(contactID: String): Uri? {

        val uriCursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts._ID),
            ContactsContract.RawContacts.CONTACT_ID + "=" + contactID, null, null
        )

        if (uriCursor == null) {
            Log.e(tag, "uriCursor is null.")
            return null
        }

        val rawContactUri: Uri

        if (uriCursor.moveToFirst()) {
            val contentUri = ContactsContract.RawContacts.CONTENT_URI
            val rawPath = "" + uriCursor.getLong(0)
            rawContactUri = contentUri.buildUpon().appendPath(rawPath).build()
        } else {
            uriCursor.close()
            return null
        }
        uriCursor.close()

        return rawContactUri
    }

    private fun getContactPhotoID(rawContactUri: Uri): Int {
        val photoSelection = (ContactsContract.Data.RAW_CONTACT_ID + "=="
                + ContentUris.parseId(rawContactUri)
                + " AND "
                + ContactsContract.RawContacts.Data.MIMETYPE + "=='"
                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'")

        val existingPhotoCursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI, null,
            photoSelection, null, null
        )

        var photoId = -1

        if (existingPhotoCursor != null) {
            val index = existingPhotoCursor.getColumnIndex(ContactsContract.Data._ID)
            if (index > 0 && existingPhotoCursor.moveToFirst()) {
                photoId = existingPhotoCursor.getInt(index)
            }
            existingPhotoCursor.close()
        }

        return photoId
    }

    private fun overwriteHiResPhoto(
        contentResolver: ContentResolver,
        contactUri: Uri,
        hiResBitmap: Bitmap?
    ) {

        val displayPhotoUri: Uri
        displayPhotoUri = Uri.withAppendedPath(
            contactUri,
            ContactsContract.Contacts.Photo.DISPLAY_PHOTO
        )
        var descriptor: AssetFileDescriptor? = null
        try {
            descriptor = contentResolver.openAssetFileDescriptor(displayPhotoUri, "w")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (descriptor != null) {
            val os: OutputStream
            try {
                os = descriptor.createOutputStream()
                hiResBitmap?.compress(Bitmap.CompressFormat.WEBP, 100, os)
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun downscaleAndCompressIntoBytes(bitmap: Bitmap?): ByteArray {
        val downscaledImageStream = ByteArrayOutputStream()

        if (bitmap != null) {
            val downscaledBitmap = downscaleBitmap(bitmap)
            downscaledBitmap.compress(downscaledImageFormat, 100, downscaledImageStream)
        }

        val bytes =  downscaledImageStream.toByteArray()
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
        private const val DOWNSCALED_IMAGE_WIDTH = 256
        private const val DOWNSCALED_IMAGE_HEIGHT = DOWNSCALED_IMAGE_WIDTH
        private const val DOWNSCALED_IMAGE_COMPRESSION_QUALITY = 100
        private const val FILTER_DOWNSCALE = true
        private val downscaledImageFormat = Bitmap.CompressFormat.JPEG
    }
}
