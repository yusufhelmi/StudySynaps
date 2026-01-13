package com.example.studysynaps

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.view.WindowCompat // <-- 1. IMPORT INI

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- 2. TAMBAHKAN KODE INI ---
        // Memberitahu sistem untuk menggunakan ikon status bar yang terang (putih)
        // karena background kita gelap.
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
        // -------------------------

        setContentView(R.layout.activity_login)

        // ... Sisa kode Anda (mencari header_block, form_container, setOnApplyWindowInsetsListener) ...
        // (Kode ini sudah benar, tidak perlu diubah)
        val headerBlock = findViewById<View>(R.id.header_block)
        val formContainer = findViewById<View>(R.id.form_container)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            headerBlock.updatePadding(top = systemBars.top)
            formContainer.updatePadding(bottom = systemBars.bottom)
            insets
        }

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val etEmail = findViewById<android.widget.EditText>(R.id.et_email)
        val etPassword = findViewById<android.widget.EditText>(R.id.et_password)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(this, "Email dan Password harus diisi", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            com.example.studysynaps.network.RetrofitClient.instance.login(email, password).enqueue(object : retrofit2.Callback<com.example.studysynaps.models.AuthResponse> {
                override fun onResponse(call: retrofit2.Call<com.example.studysynaps.models.AuthResponse>, response: retrofit2.Response<com.example.studysynaps.models.AuthResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val user = response.body()?.data
                        // Simpan Session
                        val sessionManager = com.example.studysynaps.models.SessionManager(this@Login)
                        sessionManager.createLoginSession(
                            user?.id?.toString() ?: "0", 
                            user?.fullname ?: "", 
                            user?.email ?: "",
                            user?.nim ?: "-",
                            user?.prodi ?: "-",
                            user?.status ?: "active",
                            user?.paymentStatus ?: "unpaid",
                            user?.photo
                        )

                        android.widget.Toast.makeText(this@Login, "Login Berhasil!", android.widget.Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, home::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    } else {
                        android.widget.Toast.makeText(this@Login, "Login Gagal: ${response.body()?.message}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<com.example.studysynaps.models.AuthResponse>, t: Throwable) {
                    android.widget.Toast.makeText(this@Login, "Error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
                }
            })
        }

        setupListeners()
    }

    private fun setupListeners() {
        findViewById<TextView>(R.id.tv_register_link).setOnClickListener {
            startActivity(Intent(this, register::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        findViewById<TextView>(R.id.tv_forgot_password).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            // Navigate back to IntroScreen1
            val intent = Intent(this, IntroScreen1::class.java)
            // Clear back stack so user can't go back to Login page easily if intended
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}
