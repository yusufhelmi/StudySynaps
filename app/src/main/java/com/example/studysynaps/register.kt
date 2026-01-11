package com.example.studysynaps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat // <-- Import yang diperlukan
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengaktifkan mode Edge-to-Edge
        enableEdgeToEdge()

        // --- TAMBAHKAN KODE INI ---
        // Kode ini secara manual memberitahu sistem bahwa background status bar
        // adalah gelap, sehingga ikon harus berwarna terang (putih).
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        // --- AKHIR KODE TAMBAHAN ---

        setContentView(R.layout.activity_register)

        val headerBlock = findViewById<View>(R.id.header_block)
        val formContainer = findViewById<View>(R.id.form_container)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            headerBlock.updatePadding(top = systemBars.top)
            formContainer.updatePadding(bottom = systemBars.bottom)
            insets
        }

        // Kode untuk tombol-tombol Anda
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, LoginOrRegist::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val btnSignUp = findViewById<Button>(R.id.btn_signup)
        val etFullname = findViewById<android.widget.EditText>(R.id.et_fullname)
        val etEmail = findViewById<android.widget.EditText>(R.id.et_email)
        val etPassword = findViewById<android.widget.EditText>(R.id.et_password)
        val etNim = findViewById<android.widget.EditText>(R.id.et_nim)
        val etProdi = findViewById<android.widget.EditText>(R.id.et_prodi)

        btnSignUp.setOnClickListener {
            val fullname = etFullname.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val nim = etNim.text.toString().trim()
            val prodi = etProdi.text.toString().trim()

            if (fullname.isEmpty() || email.isEmpty() || password.isEmpty() || nim.isEmpty()) {
                android.widget.Toast.makeText(this, "Semua data harus diisi!", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            com.example.studysynaps.network.RetrofitClient.instance.register(fullname, email, password, nim, prodi).enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<Any>> {
                override fun onResponse(call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>, response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<Any>>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        android.widget.Toast.makeText(this@register, "Registrasi Berhasil! Silakan Login.", android.widget.Toast.LENGTH_LONG).show()
                        val intent = Intent(this@register, Login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        android.widget.Toast.makeText(this@register, "Gagal: ${response.body()?.message}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>, t: Throwable) {
                    android.widget.Toast.makeText(this@register, "Error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
                }
            })
        }

        val btnLogin = findViewById<TextView>(R.id.tv_login_link)
        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}
