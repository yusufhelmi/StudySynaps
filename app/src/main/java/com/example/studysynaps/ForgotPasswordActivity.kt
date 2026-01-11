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
        val mainView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 3. Inisialisasi Listener tombol
        setupListeners()
    }

    private fun setupListeners() {

        // Tombol Kirim Kode (btnSendCode)
        val btnSendCode = findViewById<Button>(R.id.btnSendCode)
        btnSendCode.setOnClickListener {
            val emailInput = findViewById<EditText>(R.id.etEmail).text.toString().trim()

            if (emailInput.isNotEmpty()) {
                // Eksekusi pengiriman data ke server menggunakan Retrofit
                sendResetRequest(emailInput)
            } else {
                Toast.makeText(this, "Silakan masukkan email terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendResetRequest(email: String) {
        // Memanggil interface ApiService melalui singleton RetrofitClient
        RetrofitClient.instance.forgotPassword(email).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null && authResponse.status) {
                        // Jika server mengembalikan success = true
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            authResponse.message,
                            Toast.LENGTH_LONG
                        ).show()
                        finish() // Menutup activity dan kembali ke layar Login
                    } else {
                        // Jika email tidak terdaftar (success = false dari PHP)
                        val msg = authResponse?.message ?: "Gagal mengirim permintaan"
                        Toast.makeText(this@ForgotPasswordActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Jika terjadi error pada server (misal 404 atau 500)
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Server bermasalah. Silakan coba lagi nanti.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                // Jika tidak ada koneksi internet atau URL salah
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Koneksi gagal: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
