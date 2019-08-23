package eu.ezytarget.micopi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders


class MainActivity : AppCompatActivity() {

    var contactPickerIntentBuilder: ContactPickerIntentBuilder = ContactPickerIntentBuilder()
    lateinit var viewModel: LaunchViewModel

    /*
    Activity Life Cycle
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    fun onContactPickerButtonClicked(view: View) {
        viewModel.onContactPickerButtonClicked(activity = this)
    }

    /*
    Implementations
     */

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(LaunchViewModel::class.java)
        viewModel.selectionListener = object : LaunchSelectionListener {
            override fun onContactPickerSelected(allowMultipleSelection: Boolean) {
                startContactPickerIntent(allowMultipleSelection)
            }
        }
    }

    private fun startContactPickerIntent(allowMultipleSelection: Boolean) {
        contactPickerIntentBuilder.startIntent(
            allowMultipleSelection,
            sourceActivity = this,
            requestCode = CONTACT_PICKER_REQUEST_CODE
        )
    }


    companion object {
        private const val CONTACT_PICKER_REQUEST_CODE = 300
    }
}
