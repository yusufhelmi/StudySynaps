package com.example.finalprojectbp2uts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Atur icon status bar menjadi gelap (karena background atas putih)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContentView(R.layout.activity_profile)

        setupEdgeToEdge()
        setupMenu()
        setupFooter()
    }

    private fun setupEdgeToEdge() {
        val contentContainer: ConstraintLayout = findViewById(R.id.content_container)
        ViewCompat.setOnApplyWindowInsetsListener(contentContainer) { view, insets ->
            // Kita hanya butuh padding bawah untuk footer, jadi untuk konten utama tidak perlu padding atas
            insets
        }
    }

    private fun setupMenu() {
        // Panggil fungsi untuk setiap item menu
        setupMenuItem(
            view = findViewById(R.id.menu_detail_profile),
            iconResId = R.drawable.ic_profile,
            title = "Detail Profile",
            subtitle = "Information Account"
        ) {
            // Aksi saat diklik
            Toast.makeText(this, "Detail Profile diklik", Toast.LENGTH_SHORT).show()
        }

        setupMenuItem(
            view = findViewById(R.id.menu_privacy_account),
            iconResId = R.drawable.ic_settings,
            title = "Privacy Account",
            subtitle = "Edit Account"
        ) {
            Toast.makeText(this, "Privacy Account diklik", Toast.LENGTH_SHORT).show()
        }

        setupMenuItem(
            view = findViewById(R.id.menu_help_center),
            iconResId = R.drawable.ic_info,
            title = "Help Center",
            subtitle = "Information Details"
        ) {
            Toast.makeText(this, "Help Center diklik", Toast.LENGTH_SHORT).show()
        }

        setupMenuItem(
            view = findViewById(R.id.menu_privacy_policy),
            iconResId = R.drawable.ic_shield,
            title = "Privacy Policy",
            subtitle = "Information Details"
        ) {
            // Navigasi ke PrivacyPolicyActivity tanpa animasi
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        // Pengaturan khusus untuk tombol Logout
        val logoutView = findViewById<View>(R.id.menu_logout)
        val logoutTitle = logoutView.findViewById<TextView>(R.id.tv_menu_title)
        val logoutIcon = logoutView.findViewById<ImageView>(R.id.iv_menu_icon)

        logoutTitle.text = "Logout"
        logoutTitle.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        logoutView.findViewById<TextView>(R.id.tv_menu_subtitle).visibility = View.GONE
        logoutIcon.setImageResource(R.drawable.ic_logout)
        logoutIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark))

        logoutView.setOnClickListener {
            // Kembali ke halaman Login tanpa animasi
            val intent = Intent(this, LoginOrRegist::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    // Fungsi bantuan untuk mengurangi duplikasi kode
    private fun setupMenuItem(view: View, iconResId: Int, title: String, subtitle: String, onClick: () -> Unit) {
        view.findViewById<ImageView>(R.id.iv_menu_icon).setImageResource(iconResId)
        view.findViewById<TextView>(R.id.tv_menu_title).text = title
        view.findViewById<TextView>(R.id.tv_menu_subtitle).text = subtitle
        view.setOnClickListener { onClick() }
    }

    private fun setupFooter() {
        // Logika untuk footer
        val footer = findViewById<ConstraintLayout>(R.id.footer_container)
        ViewCompat.setOnApplyWindowInsetsListener(footer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            windowInsets
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavView.selectedItemId = R.id.nav_profil // Set "Profil" sebagai item aktif

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, home::class.java))
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
                R.id.nav_jadwal -> {
                    startActivity(Intent(this, JadwalActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profil -> true // Sudah di halaman ini
                else -> {
                    Toast.makeText(this, "Menu belum tersedia", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
    }
}
