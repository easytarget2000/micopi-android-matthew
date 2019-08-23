package eu.ezytarget.micopi

import android.app.Activity
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker
import android.R
import android.R.attr.colorPrimary
import android.graphics.Color
import androidx.core.content.ContextCompat


class ContactPickerIntentBuilder {

    fun startIntent(sourceActivity: Activity, requestCode: Int) {
//        val context = sourceActivity.applicationContext
        MultiContactPicker.Builder(sourceActivity)
//            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
            .searchIconColor(Color.WHITE) //Option - default: White
            .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE)
            .setLoadingType(MultiContactPicker.LOAD_ASYNC)
            .limitToColumn(LimitColumn.NONE)
            .showPickerForResult(requestCode)
    }
}