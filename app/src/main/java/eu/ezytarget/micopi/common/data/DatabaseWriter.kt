package eu.ezytarget.micopi.common.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

object DatabaseWriter {

    private val TAG = DatabaseWriter::class.java.simpleName

    fun assignImageToContact(
        context: Context,
        hiResBitmap: Bitmap?,
        contact: Contact
    ): Boolean {
        val contentResolver = context.contentResolver

        val rawContactUri = getContactUri(contentResolver, contact.databaseID) ?: return false

        val values = ContentValues()
        values.put(
            ContactsContract.Data.RAW_CONTACT_ID,
            ContentUris.parseId(rawContactUri)
        )
        values.put(
            ContactsContract.Data.IS_SUPER_PRIMARY,
            1
        )

        val outputStream = ByteArrayOutputStream()

        if (hiResBitmap != null) {
            val scaledBitmap = Bitmap.createScaledBitmap(
                hiResBitmap,
                256,
                256,
                true
            )
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        values.put(
            ContactsContract.CommonDataKinds.Photo.PHOTO,
            outputStream.toByteArray()
        )

        try {
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        values.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        )

        val photoId = getContactPhotoId(contentResolver, rawContactUri)

        if (photoId >= 0) {
            contentResolver.update(
                ContactsContract.Data.CONTENT_URI,
                values,
                ContactsContract.Data._ID + "=" + photoId, null
            )
        } else {
            contentResolver.insert(
                ContactsContract.Data.CONTENT_URI,
                values
            )
        }

        overwriteHiResPhoto(contentResolver, rawContactUri, hiResBitmap)

        return true
    }

    private fun getContactUri(
        contentResolver: ContentResolver,
        contactId: String
    ): Uri? {

        val uriCursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts._ID),
            ContactsContract.RawContacts.CONTACT_ID + "=" + contactId, null, null
        )

        if (uriCursor == null) {
            Log.e(TAG, "uriCursor is null.")
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

    private fun getContactPhotoId(
        contentResolver: ContentResolver,
        rawContactUri: Uri
    ): Int {
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

}
