package com.example.finalprojectbp2uts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PilihUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnPengajar = findViewById<Button>(R.id.buttonPengajar)
        btnPengajar.setOnClickListener {
            val intent = Intent(this, PilihUser::class.java)
            startActivity(intent)
            finish()
        }

        // 1. Temukan tombol "Lanjutkan" (pastikan ID di XML sesuai)
        val btnBack = findViewById<TextView>(R.id.textViewBack) // Ganti R.id.btnLanjutkan jika ID Anda beda

        // 2. Beri fungsi saat tombol diklik
        btnBack.setOnClickListener {
            // 3. Buat "Intent" untuk pindah ke layar berikutnya
            // (Ganti 'introScreen2::class.java' dengan nama Activity ke-2 Anda)
            val intent = Intent(this, IntroScreen3::class.java)

            // 4. Jalankan perintah pindah halaman
            startActivity(intent)

            // 5. Tutup layar ini agar tombol "Back" tidak kembali ke sini
            finish()
        }

        val btnMhs = findViewById<Button>(R.id.buttonMahasiwa)
        btnMhs.setOnClickListener {
            val intent = Intent(this, LoginOrRegist::class.java)
            startActivity(intent)
            finish()
        }
    }
}