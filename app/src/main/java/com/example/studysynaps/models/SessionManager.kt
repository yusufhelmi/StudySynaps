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
        const val KEY_STATUS = "status"
        const val KEY_PAYMENT_STATUS = "payment_status"
        const val KEY_USER_PHOTO = "user_photo"
    }

    fun createLoginSession(id: String, fullname: String, email: String, nim: String, prodi: String, status: String, paymentStatus: String, photo: String?) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGIN, true)
        editor.putString(KEY_USER_ID, id)
        editor.putString(KEY_FULLNAME, fullname)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NIM, nim)
        editor.putString(KEY_PRODI, prodi)
        editor.putString(KEY_STATUS, status)
        editor.putString(KEY_PAYMENT_STATUS, paymentStatus)
        if (photo != null) {
            editor.putString(KEY_USER_PHOTO, photo)
        }
        editor.apply()
    }
    
    fun updateStatus(status: String) {
        val editor = prefs.edit()
        editor.putString(KEY_STATUS, status)
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
    
    fun getUserStatus(): String? {
        // Default to 'active' if not found to avoid blocking existing users who haven't re-logged in
        return prefs.getString(KEY_STATUS, "active") 
    }

    fun saveUserPhoto(url: String) {
        val editor = prefs.edit()
        editor.putString(KEY_USER_PHOTO, url)
        editor.apply()
    }

    fun getUserPhoto(): String? {
        return prefs.getString(KEY_USER_PHOTO, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
