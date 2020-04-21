package io.github.ibonotegui.boxofrain.util

import android.content.Context
import androidx.preference.PreferenceManager

object BoxPreferences {

    private const val LATITUDE_PREF_KEY = "latitude_pref_key"
    private const val LONGITUDE_PREF_KEY = "longitude_pref_key"

    fun getLocation(context: Context): Pair<String, String>? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if(sharedPreferences.getString(LATITUDE_PREF_KEY, null) != null &&
            sharedPreferences.getString(LONGITUDE_PREF_KEY, null) != null) {
            val latitude = sharedPreferences.getString(LATITUDE_PREF_KEY, "")
            val longitude = sharedPreferences.getString(LONGITUDE_PREF_KEY, "")
            return Pair(latitude, longitude) as Pair<String, String>
        }
        return null
    }

    fun setLocation(context: Context, latitude: String, longitude: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putString(LATITUDE_PREF_KEY, latitude)
        prefsEditor.putString(LONGITUDE_PREF_KEY, longitude)
        prefsEditor.apply()
    }

}
