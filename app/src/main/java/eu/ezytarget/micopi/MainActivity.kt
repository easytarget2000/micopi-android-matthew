package eu.ezytarget.micopi

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders


class MainActivity : AppCompatActivity() {

    var contactPickerIntent = Intent(
        Intent.ACTION_PICK,
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    )
    lateinit var viewModel: LaunchViewModel

    /*
    Activity Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
    }

    fun onContactPickerButtonClicked(view: View) {
        viewModel.onContactPickerButtonClicked()
    }

    /*
    Implementations
     */

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(LaunchViewModel::class.java)
        viewModel.listener = object: LaunchSelectionListener {
            override fun onContactPickerSelected() {
                startContactPickerIntent()
            }
        }
    }

    private fun startContactPickerIntent() {
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_REQUEST_CODE)
    }



    companion object {
        private const val CONTACT_PICKER_REQUEST_CODE = 300
    }
}
