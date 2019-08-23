package eu.ezytarget.micopi

import androidx.lifecycle.ViewModel

class LaunchViewModel: ViewModel() {

    var listener: LaunchSelectionListener? = null

    fun onContactPickerButtonClicked() {
        listener?.onContactPickerSelected()
    }
}