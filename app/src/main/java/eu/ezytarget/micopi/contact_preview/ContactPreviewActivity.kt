package eu.ezytarget.micopi.contact_preview

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.Activity
import eu.ezytarget.micopi.common.ContactHashWrapper
import eu.ezytarget.micopi.databinding.ContactPreviewActivityBinding

class ContactPreviewActivity : Activity() {

//    private lateinit var viewModel: ContactPreviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = setupViewModel()
        val binding = setupBinding(viewModel)
    }

    private fun setupViewModel(): ContactPreviewViewModel {
        val viewModel = getViewModel(ContactPreviewViewModel::class)
        val contactHashWrapper = intent.extras!![CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME]
                as ContactHashWrapper

//        val contactNameObserver = Observer<String> {
//            showContactName(it)
//        }
        viewModel.contactHashWrapper = contactHashWrapper

        return viewModel
    }

    private fun setupBinding(viewModel: ContactPreviewViewModel): ViewDataBinding {
        val binding: ContactPreviewActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.contact_preview_activity)

        binding.viewModel = viewModel
        return binding
    }

    private fun showContactName(contactName: String) {
        val contactNameView: TextView = findViewById(R.id.contactPreviewNameView)
        contactNameView.text = contactName
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.CONTACT_PREVIEW"
        const val CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPER"
    }
}