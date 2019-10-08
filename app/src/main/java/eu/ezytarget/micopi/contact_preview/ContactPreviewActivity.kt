package eu.ezytarget.micopi.contact_preview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import eu.ezytarget.micopi.BuildConfig
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.Activity
import eu.ezytarget.micopi.common.ui.ViewModelMessageListener
import eu.ezytarget.micopi.contact_preview.ads.AdsLoader
import eu.ezytarget.micopi.databinding.ContactPreviewActivityBinding


class ContactPreviewActivity : Activity() {

    var adsLoader: AdsLoader = AdsLoader()
    private val viewModel: ContactPreviewViewModel by lazy {
        getViewModel(ContactPreviewViewModel::class)
    }
    private val shareIntent: Intent by lazy {
        Intent(Intent.ACTION_SEND)
    }

    /*
    Activityy Lifecycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
        setupAdsPresenter()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /*
    Implementations
     */

    private fun setupViewModel() {
        viewModel.resources = resources
        viewModel.contentResolver = contentResolver
        viewModel.messageListener = object: ViewModelMessageListener {
            override fun onMessageRequested(message: String) {
                showMessage(message)
            }
        }
        viewModel.listener = object : ContactPreviewViewModelListener {
            override fun onImageUriSharingRequested(imageUri: Uri) {
                shareImageUri(imageUri)
            }

            override fun onImageAssigned() {
                showAdIfAvailable()
            }
        }
        viewModel.setupTracker(getFirebaseInstance())

        val contactHashWrapper = intent.extras!![CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME]
                as ContactHashWrapper
        viewModel.contactHashWrapper = contactHashWrapper
    }

    private fun setupDataBinding() {
        val binding: ContactPreviewActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.contact_preview_activity
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupAdsPresenter() {
        val adUnitID = if (BuildConfig.DEBUG) {
            AdsLoader.TEST_AD_MOB_INTERSTITIAL_AD_ID
        } else {
            getString(R.string.adMobContactPreviewInterstitialAdID)
        }
        adsLoader.setup(this, adUnitID)
    }

    private fun shareImageUri(imageUri: Uri) {
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(imageUri, contentResolver.getType(imageUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        val chooserTitle = getString(R.string.contactPreviewSharingAppChooserTitle)
        val chooser = Intent.createChooser(shareIntent, chooserTitle)
        startActivity(chooser)
    }

    private fun showAdIfAvailable() {
        adsLoader.showAdIfAvailable()
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.CONTACT_PREVIEW"
        const val CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPER"
    }
}