package io.github.ibonotegui.boxofrain.util

import android.content.Context
import androidx.preference.PreferenceManager
import io.github.ibonotegui.boxofrain.model.Location

object BoxPreferences {

    private const val NAME_PREF_KEY = "name_pref_key"
    private const val LATITUDE_PREF_KEY = "latitude_pref_key"
    private const val LONGITUDE_PREF_KEY = "longitude_pref_key"

    fun getLocation(context: Context): Location? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if(sharedPreferences.getString(LATITUDE_PREF_KEY, null) != null &&
            sharedPreferences.getString(LONGITUDE_PREF_KEY, null) != null) {
            val name = sharedPreferences.getString(NAME_PREF_KEY, "") as String
            val latitude = sharedPreferences.getString(LATITUDE_PREF_KEY, "") as String
            val longitude = sharedPreferences.getString(LONGITUDE_PREF_KEY, "") as String
            return Location(name, latitude, longitude)
        }
        return null
    }

    fun setLocation(context: Context, name: String, latitude: String, longitude: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = sharedPreferences.edit()
        prefsEditor.putString(NAME_PREF_KEY, name)
        prefsEditor.putString(LATITUDE_PREF_KEY, latitude)
        prefsEditor.putString(LONGITUDE_PREF_KEY, longitude)
        prefsEditor.apply()
    }

}
