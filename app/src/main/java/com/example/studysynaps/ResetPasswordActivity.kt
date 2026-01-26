package com.example.studysynaps

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studysynaps.network.RetrofitClient
import com.example.studysynaps.models.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        // Get User ID from Intent
        userId = intent.getIntExtra("USER_ID", 0)

        // Validate ID
        if (userId == 0) {
            Toast.makeText(this, "Kesalahan Sistem: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSave = findViewById<Button>(R.id.btnSavePassword)
        btnSave.setOnClickListener {
            val pass1 = findViewById<EditText>(R.id.etNewPassword).text.toString()
            val pass2 = findViewById<EditText>(R.id.etConfirmPassword).text.toString()

            if (pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
            } else if (pass1 != pass2) {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
            } else {
                savePassword(pass1)
            }
        }
    }

    private fun savePassword(newPass: String) {
        RetrofitClient.instance.resetPassword(userId, newPass).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(this@ResetPasswordActivity, "Password Berhasil Diubah! Silakan Login.", Toast.LENGTH_LONG).show()
                    finish() // Close Reset Activity
                } else {
                    Toast.makeText(this@ResetPasswordActivity, "Gagal mengubah password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                Toast.makeText(this@ResetPasswordActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
