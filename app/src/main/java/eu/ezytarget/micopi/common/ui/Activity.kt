package eu.ezytarget.micopi.common.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.analytics.FirebaseAnalytics
import kotlin.reflect.KClass

abstract class Activity: AppCompatActivity() {

    val viewModelProvider: ViewModelProvider
    get() = ViewModelProviders.of(this)
    protected lateinit var firebaseInstance: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebaseInstance()
    }

    private fun initFirebaseInstance() {
        firebaseInstance = FirebaseAnalytics.getInstance(this)
    }

    fun <T : ViewModel> getViewModel(modelClass: KClass<T>): T {
        return viewModelProvider.get(modelClass.java)
    }
}