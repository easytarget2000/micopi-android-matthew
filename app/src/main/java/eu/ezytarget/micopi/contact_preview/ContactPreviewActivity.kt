package eu.ezytarget.micopi.contact_preview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.Activity
import eu.ezytarget.micopi.databinding.ContactPreviewActivityBinding


class ContactPreviewActivity : Activity() {

    private val viewModel: ContactPreviewViewModel by lazy {
        getViewModel(ContactPreviewViewModel::class)
    }

    private val shareIntent: Intent by lazy {
        Intent(Intent.ACTION_SEND)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    private fun setupViewModel() {
        val contactHashWrapper = intent.extras!![CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME]
                as ContactHashWrapper

        viewModel.resources = resources
        viewModel.contentResolver = contentResolver
        viewModel.contactHashWrapper = contactHashWrapper
        viewModel.listener = object : ContactPreviewViewModelListener{
            override fun onImageUriSharingRequested(imageUri: Uri) {
                shareImageUri(imageUri)
            }
        }
    }

    private fun setupDataBinding() {
        val binding: ContactPreviewActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.contact_preview_activity
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun shareImageUri(imageUri: Uri) {
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(imageUri, contentResolver.getType(imageUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        val chooserTitle = getString(R.string.contactPreviewSharingAppChooserTitle)
        val chooser = Intent.createChooser(shareIntent, chooserTitle)
        startActivity(chooser)
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.CONTACT_PREVIEW"
        const val CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPER"
    }
}