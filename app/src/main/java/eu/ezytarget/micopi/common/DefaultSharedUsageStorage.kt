package eu.ezytarget.micopi.common

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class DefaultSharedUsageStorage {

    private lateinit var sharedPreferences: SharedPreferences

    fun setup(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getBoolean(preferencesKey: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(preferencesKey, defaultValue)
    }

    fun setValueAsync(pair: Pair<String, Any>) {
        val preferencesKey = pair.first
        val value = pair.second
        val preferencesEditor = sharedPreferences.edit()
        when (value) {
            is String -> preferencesEditor.putString(preferencesKey, value)
            is Int -> preferencesEditor.putInt(preferencesKey, value)
            is Boolean -> preferencesEditor.putBoolean(preferencesKey, value)
            is Long -> preferencesEditor.putLong(preferencesKey, value)
            is Float -> preferencesEditor.putFloat(preferencesKey, value)
        }
        preferencesEditor.apply()
    }
}