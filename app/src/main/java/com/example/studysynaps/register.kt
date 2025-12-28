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
        btnSignUp.setOnClickListener {
            val intent = Intent(this, home::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
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
