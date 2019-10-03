package eu.ezytarget.micopi.main_menu

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.ui.Activity
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.contact_preview.ContactPreviewActivity
import eu.ezytarget.micopi.contact_preview.ContactPreviewViewModel
import eu.ezytarget.micopi.databinding.ContactPreviewActivityBinding
import eu.ezytarget.micopi.databinding.MainMenuActivityBinding


class MainMenuActivity : Activity() {

    var contactPickerIntentBuilder: ContactPickerIntentBuilder =
        ContactPickerIntentBuilder()
    private val viewModel: MainMenuViewModel by lazy {
        getViewModel(MainMenuViewModel::class)
    }

    /*
    Activity Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_PICKER_REQUEST_CODE) {
            viewModel.handleContactPickerData(data)
        }
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
        viewModel.setup(this, getFirebaseInstance())
        viewModel.selectionListener = object : MainMenuSelectionListener {
            override fun onContactPickerSelected(allowMultipleSelection: Boolean) {
                startContactPickerIntent(allowMultipleSelection)
            }

            override fun onContactSelected(contactHashWrapper: ContactHashWrapper) {
                startContactPreviewActivity(contactHashWrapper)
            }
        }
        viewModel.paymentFlowListener = object : PaymentFlowListener {
            override fun onPaymentFlowPlusProductPurchased() {
                showPlusProductThankYouDialog()
            }
        }
    }

    private fun setupDataBinding() {
        val binding: MainMenuActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_menu_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun startContactPickerIntent(allowMultipleSelection: Boolean) {
        contactPickerIntentBuilder.startIntent(
            allowMultipleSelection,
            sourceActivity = this,
            requestCode = CONTACT_PICKER_REQUEST_CODE
        )
    }

    private fun startContactPreviewActivity(contactHashWrapper: ContactHashWrapper) {
        val contactPreviewIntent = Intent(ContactPreviewActivity.ACTION_IDENTIFIER)
        contactPreviewIntent.putExtra(
            ContactPreviewActivity.CONTACT_HASH_WRAPPER_INTENT_EXTRA_NAME,
            contactHashWrapper
        )
        startActivity(contactPreviewIntent)
    }

    private fun showPlusProductThankYouDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.mainMenuCapabilitiesCardPostPurchaseCopy)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    companion object {
        private const val CONTACT_PICKER_REQUEST_CODE = 300
    }
}
