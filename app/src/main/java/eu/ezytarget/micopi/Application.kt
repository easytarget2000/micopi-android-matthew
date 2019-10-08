package eu.ezytarget.micopi

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication

class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}