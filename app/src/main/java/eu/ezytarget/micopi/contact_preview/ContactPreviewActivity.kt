package eu.ezytarget.micopi.contact_preview

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.Activity
import eu.ezytarget.micopi.common.ContactHashWrapper
import eu.ezytarget.micopi.databinding.ContactPreviewActivityBinding

class ContactPreviewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = setupViewModel()
        setupDataBinding(viewModel)
    }

    private fun setupViewModel(): ContactPreviewViewModel {
        val viewModel = getViewModel(ContactPreviewViewModel::class)
        val contactHashWrapper = intent.extras!![CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME]
                as ContactHashWrapper

        viewModel.contactHashWrapper = contactHashWrapper
        return viewModel
    }

    private fun setupDataBinding(viewModel: ContactPreviewViewModel) {
        val binding: ContactPreviewActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.contact_preview_activity)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.CONTACT_PREVIEW"
        const val CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPER"
    }
}