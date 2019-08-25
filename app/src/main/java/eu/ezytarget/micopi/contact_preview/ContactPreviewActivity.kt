package eu.ezytarget.micopi.contact_preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eu.ezytarget.micopi.R

class ContactPreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_preview_activity)

    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.CONTACT_PREVIEW"
        const val CONTACT_INTENT_EXTRA_NAME = "CONTACT"
    }
}