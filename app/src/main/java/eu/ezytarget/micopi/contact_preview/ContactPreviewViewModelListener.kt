package eu.ezytarget.micopi.contact_preview

import android.net.Uri

interface ContactPreviewViewModelListener {

    fun onMessageRequested(message: String)
    fun onImageUriSharingRequested(imageUri: Uri)
}