package com.example.finalprojectbp2uts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginOrRegist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_or_regist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<TextView>(R.id.textViewBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, IntroScreen3::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val btnDaftar = findViewById<Button>(R.id.buttonDaftar)
        btnDaftar.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val btnMasuk = findViewById<Button>(R.id.buttonMasuk)
        btnMasuk.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}