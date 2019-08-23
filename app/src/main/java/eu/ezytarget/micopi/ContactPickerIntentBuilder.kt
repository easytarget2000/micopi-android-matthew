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
        val context = sourceActivity.applicationContext
        MultiContactPicker.Builder(sourceActivity)
//            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
            .hideScrollbar(false) //Optional - default: false
            .showTrack(true) //Optional - default: true
            .searchIconColor(Color.WHITE) //Option - default: White
            .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
//            .handleColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.
//                )
//            ) //Optional - default: Azure Blue
//            .bubbleColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.colorPrimary
//                )
//            ) //Optional - default: Azure Blue
            .bubbleTextColor(Color.WHITE) //Optional - default: White
            .setTitleText("Select Contacts ~DEBUG~") //Optional - default: Select Contacts
//            .setSelectedContacts(
//                "10",
//                "5"
//            ) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
            .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
            .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
            .setActivityAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ) //Optional - default: No animation overrides
            .showPickerForResult(requestCode)
    }
}