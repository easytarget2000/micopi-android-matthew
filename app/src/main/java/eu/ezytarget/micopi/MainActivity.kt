package eu.ezytarget.micopi

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.onegravity.contactpicker.contact.ContactDescription
import com.onegravity.contactpicker.contact.ContactSortOrder
import com.onegravity.contactpicker.core.ContactPickerActivity
import com.onegravity.contactpicker.picture.ContactPictureType


class MainActivity : AppCompatActivity() {

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
            override fun onContactPickerSelected() {
                startContactPickerIntent()
            }
        }
    }

    private fun startContactPickerIntent() {
        var contactPickerIntent = Intent(this, ContactPickerActivity::class.java)
            .putExtra(
                ContactPickerActivity.EXTRA_THEME,
                R.style.AppTheme
            )
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                ContactPictureType.ROUND.name
            )
            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                ContactDescription.ADDRESS.name
            )
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                ContactsContract.CommonDataKinds.Email.TYPE_WORK
            )
            .putExtra(
                ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                ContactSortOrder.AUTOMATIC.name
            )
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_REQUEST_CODE)
    }


    companion object {
        private const val CONTACT_PICKER_REQUEST_CODE = 300
    }
}
