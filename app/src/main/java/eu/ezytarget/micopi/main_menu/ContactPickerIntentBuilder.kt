package eu.ezytarget.micopi.main_menu

import android.app.Activity
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker


class ContactPickerIntentBuilder {

    fun startIntent(
        allowMultipleSelection: Boolean,
        sourceActivity: Activity,
        requestCode: Int
    ) {
        val choiceMode = if (allowMultipleSelection) {
            MultiContactPicker.CHOICE_MODE_MULTIPLE
        } else {
            MultiContactPicker.CHOICE_MODE_SINGLE
        }
        MultiContactPicker.Builder(sourceActivity)
            .setChoiceMode(choiceMode)
            .setLoadingType(MultiContactPicker.LOAD_ASYNC)
            .limitToColumn(LimitColumn.NONE)
            .showPickerForResult(requestCode)
    }
}