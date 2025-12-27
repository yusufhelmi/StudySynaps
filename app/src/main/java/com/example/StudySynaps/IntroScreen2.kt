package com.example.StudySynaps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IntroScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_screen2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 1. Temukan tombol "Lanjutkan" (pastikan ID di XML sesuai)
        val btnLanjutkan = findViewById<Button>(R.id.buttonLanjut) // Ganti R.id.btnLanjutkan jika ID Anda beda

        // 2. Beri fungsi saat tombol diklik
        btnLanjutkan.setOnClickListener {
            // 3. Buat "Intent" untuk pindah ke layar berikutnya
            // (Ganti 'introScreen2::class.java' dengan nama Activity ke-2 Anda)
            val intent = Intent(this, IntroScreen3::class.java)

            // 4. Jalankan perintah pindah halaman
            startActivity(intent)
            overridePendingTransition(0, 0)

            // 5. Tutup layar ini agar tombol "Back" tidak kembali ke sini
            finish()
        }

        val btnBack = findViewById<TextView>(R.id.textViewBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, IntroScreen1::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val btnLewati = findViewById<TextView>(R.id.textViewLewati)
        btnLewati.setOnClickListener {
            val intent = Intent(this, LoginOrRegist::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}