package eu.ezytarget.micopi.contact_preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.Activity
import eu.ezytarget.micopi.common.Contact

class ContactPreviewActivity : Activity() {

    private lateinit var viewModel: ContactPreviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_preview_activity)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = getViewModel(ContactPreviewViewModel::class)
        val contact = intent.extras!![CONTACT_INTENT_EXTRA_NAME] as Contact
        viewModel.contact = contact
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.CONTACT_PREVIEW"
        const val CONTACT_INTENT_EXTRA_NAME = "CONTACT"
    }
}