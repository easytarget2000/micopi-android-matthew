package eu.ezytarget.micopi.common.ui

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import kotlin.reflect.KClass

abstract class Activity : AppCompatActivity() {

    val viewModelProvider: ViewModelProvider
        get() = ViewModelProviders.of(this)

    protected fun getFirebaseInstance(): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(this)
    }

    protected fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun <T : ViewModel> getViewModel(modelClass: KClass<T>): T {
        return viewModelProvider.get(modelClass.java)
    }
}