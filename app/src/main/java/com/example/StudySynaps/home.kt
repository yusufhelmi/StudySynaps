package com.example.StudySynaps

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView

class home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Mengaktifkan mode Edge-to-Edge agar layout memenuhi layar
        enableEdgeToEdge()

        // 2. Mengatur ikon status bar (jam, baterai) menjadi hitam agar terlihat di background terang
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // 3. Menghubungkan Activity dengan layout XML-nya
        setContentView(R.layout.activity_home)

        // 4. Memanggil semua fungsi setup
        setupEdgeToEdge()
        setupFooter()
        setupMenu()
    }

    /**
     * Fungsi ini menambahkan padding atas pada konten utama
     * agar tidak tertimpa oleh status bar.
     */
    private fun setupEdgeToEdge() {
        val contentScrollView: NestedScrollView = findViewById(R.id.content_scroll_view)
        ViewCompat.setOnApplyWindowInsetsListener(contentScrollView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            // Baris ini wajib ada untuk mengembalikan insets
            windowInsets
        }
    }

    /**
     * Fungsi ini memberikan listener pada tombol menu di dalam konten,
     * seperti tombol Presensi.
     */
    private fun setupMenu() {
        findViewById<android.widget.LinearLayout>(R.id.btn_presensi).setOnClickListener {
            startActivity(Intent(this, PresensiActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<android.widget.LinearLayout>(R.id.btn_transkrip).setOnClickListener {
            startActivity(Intent(this, TranskripActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<android.widget.LinearLayout>(R.id.btn_ktm).setOnClickListener {
            startActivity(Intent(this, KtmActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<android.widget.LinearLayout>(R.id.btn_hasil_studi).setOnClickListener {
            startActivity(Intent(this, HasilStudiActivity::class.java))
            overridePendingTransition(0, 0)
        }

        // Schedule Card Listeners
        findViewById<View>(R.id.card_schedule_1)?.setOnClickListener {
            startActivity(Intent(this, MateriTugasActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<View>(R.id.card_schedule_2)?.setOnClickListener {
            startActivity(Intent(this, MateriTugasActivity::class.java))
            overridePendingTransition(0, 0)
        }
        findViewById<View>(R.id.card_schedule_3)?.setOnClickListener {
            startActivity(Intent(this, MateriTugasActivity::class.java))
            overridePendingTransition(0, 0)
        }
        
        // TODO: Tambahkan listener untuk menu lain di sini jika perlu
    }

    /**
     * Fungsi ini mengatur semua logika untuk footer, termasuk padding bawah
     * dan navigasi antar halaman.
     */
    private fun setupFooter() {
        // Mengatur padding bawah untuk footer agar tidak tertimpa navigation bar sistem
        val footer = findViewById<ConstraintLayout>(R.id.footer_container)
        ViewCompat.setOnApplyWindowInsetsListener(footer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            windowInsets
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        // Atur item "Home" sebagai yang aktif secara default di halaman ini
        bottomNavView.selectedItemId = R.id.nav_home

        // Atur listener untuk menangani klik pada setiap item navigasi
        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true // Sudah di halaman ini, tidak melakukan apa-apa
                R.id.nav_jadwal -> {
                    startActivity(Intent(this, JadwalActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_krs -> {
                    startActivity(Intent(this, KrsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profil -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}
