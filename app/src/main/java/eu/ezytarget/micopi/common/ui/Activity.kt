package eu.ezytarget.micopi.common.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.reflect.KClass

abstract class Activity: AppCompatActivity() {

    val viewModelProvider: ViewModelProvider
    get() = ViewModelProviders.of(this)

    fun <T : ViewModel> getViewModel(modelClass: KClass<T>): T {
        return viewModelProvider.get(modelClass.java)
    }
}