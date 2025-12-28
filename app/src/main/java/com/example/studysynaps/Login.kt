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

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, LoginOrRegist::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val btnRegist = findViewById<TextView>(R.id.tv_register_link)
        btnRegist.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            val intent = Intent(this, home::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }
}

