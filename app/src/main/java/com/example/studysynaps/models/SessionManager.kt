package com.example.studysynaps.models

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val KEY_IS_LOGIN = "is_login"
        const val KEY_USER_ID = "user_id"
        const val KEY_FULLNAME = "fullname"
        const val KEY_EMAIL = "email"
        const val KEY_NIM = "nim"
        const val KEY_PRODI = "prodi"
    }

    fun createLoginSession(id: String, fullname: String, email: String, nim: String, prodi: String) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGIN, true)
        editor.putString(KEY_USER_ID, id)
        editor.putString(KEY_FULLNAME, fullname)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NIM, nim)
        editor.putString(KEY_PRODI, prodi)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_FULLNAME, null)
    }

    fun getUserNim(): String? {
        return prefs.getString(KEY_NIM, null)
    }
    
    fun getUserProdi(): String? {
        return prefs.getString(KEY_PRODI, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
