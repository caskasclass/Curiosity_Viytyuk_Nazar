package it.uninsubria.curiosityapp.data.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun saveUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun saveSliderValue(value: Float) {
        prefs.edit().putFloat("ore_scelte", value).apply()
    }

    fun getSliderValue(): Float {
        return prefs.getFloat("ore_scelte", 4f)
    }
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}