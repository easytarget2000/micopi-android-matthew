package eu.ezytarget.micopi.main_menu

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.batch.BatchActivity
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.Activity
import eu.ezytarget.micopi.contact_preview.ContactPreviewActivity
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
        viewModel.setupAndGetCapabilities(this, getFirebaseInstance())
        viewModel.selectionListener = object : MainMenuSelectionListener {
            override fun onContactPickerSelected(allowMultipleSelection: Boolean) {
                startContactPickerIntent(allowMultipleSelection)
            }

            override fun onContactSelected(contactHashWrapper: ContactHashWrapper) {
                startContactPreviewActivity(contactHashWrapper)
            }

            override fun onContactsSelected(contactHashWrappers: Array<ContactHashWrapper>) {
                startBatchActivity(contactHashWrappers)
            }

            override fun sendPromoMailSelected() {
                sendPromoMail()
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

    private fun startBatchActivity(contactHashWrappers: Array<ContactHashWrapper>) {
        val batchIntent = Intent(BatchActivity.ACTION_IDENTIFIER)
        batchIntent.putExtra(
            BatchActivity.CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME,
            contactHashWrappers
        )
        startActivity(batchIntent)
    }

    private fun showPlusProductThankYouDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.mainMenuCapabilitiesCardPostPurchaseCopy)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun sendPromoMail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.data = Uri.parse(EMAIL_URI)
        emailIntent.type = EMAIL_INTENT_TYPE
        val recipient = getString(R.string.plusAppCustomerMailAddress)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        val subject = getString(R.string.plusAppCustomerMailSubject)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        try {
            startActivity(Intent.createChooser(emailIntent, recipient))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val CONTACT_PICKER_REQUEST_CODE = 300
        private const val EMAIL_URI = "mailto:"
        private const val EMAIL_INTENT_TYPE = "text/plain"
    }
}
