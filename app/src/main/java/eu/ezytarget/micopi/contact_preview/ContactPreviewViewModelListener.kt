package eu.ezytarget.micopi.contact_preview

import android.net.Uri

interface ContactPreviewViewModelListener {
    fun onImageUriSharingRequested(imageUri: Uri)
    fun onImageAssigned()
}