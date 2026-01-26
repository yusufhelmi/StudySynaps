package com.example.studysynaps

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studysynaps.models.AuthResponse
import com.example.studysynaps.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Mengaktifkan fitur Edge-to-Edge
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        // 2. Menangani Insets (Padding agar tidak tertutup Status Bar/Nav Bar)
        // Pastikan di layout XML Anda, root layout memiliki ID 'main'
        // 2. Menangani Insets
        val mainView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // 3. Inisialisasi Listener tombol
        setupListeners()
    }

    private fun setupListeners() {
        // Back Button
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.setOnClickListener {
            finish()
        }

        // Tombol Kirim Kode (btnSendCode) -> Sekarang "Verifikasi"
        val btnSendCode = findViewById<Button>(R.id.btnSendCode)
        btnSendCode.text = "Verifikasi Data" // Update button text
        
        btnSendCode.setOnClickListener {
            val emailInput = findViewById<EditText>(R.id.etEmail).text.toString().trim()
            val nimInput = findViewById<EditText>(R.id.etNim).text.toString().trim()

            if (emailInput.isNotEmpty() && nimInput.isNotEmpty()) {
                sendResetRequest(emailInput, nimInput)
            } else {
                Toast.makeText(this, "Silakan masukkan Email dan NIM", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendResetRequest(email: String, nim: String) {
        RetrofitClient.instance.forgotPassword(email, nim).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null && authResponse.status) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Verifikasi Berhasil! Silakan buat password baru.",
                            Toast.LENGTH_SHORT
                        ).show()
                        
                        // Pass User ID to Reset Password Activity
                        val intent = android.content.Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                        intent.putExtra("USER_ID", authResponse.userId)
                        startActivity(intent)
                        finish() 
                    } else {
                        val msg = authResponse?.message ?: "Data tidak ditemukan"
                        Toast.makeText(this@ForgotPasswordActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@ForgotPasswordActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
