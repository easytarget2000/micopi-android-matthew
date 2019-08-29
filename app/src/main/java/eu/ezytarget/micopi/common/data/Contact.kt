package eu.ezytarget.micopi.common.data

import android.net.Uri
import java.io.Serializable

data class Contact(
    val entityID: String,
    val displayName: String,
    val photoUri: Uri?
): Serializable {

    val hasPhoto: Boolean
    get() = photoUri != null
}