package com.example.studysynaps

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
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContentView(R.layout.activity_profile)

        setupEdgeToEdge()
        // Data populated in onResume to ensure updates match
        setupMenu()
        setupFooter()
    }

    override fun onResume() {
        super.onResume()
        setupUserData()
    }

    private fun setupEdgeToEdge() {
        val contentContainer: ConstraintLayout = findViewById(R.id.content_container)
        ViewCompat.setOnApplyWindowInsetsListener(contentContainer) { view, insets ->
            insets
        }
    }

    private fun setupUserData() {
        val sessionManager = SessionManager(this)
        val tvName = findViewById<TextView>(R.id.tv_profile_name)
        val ivProfile = findViewById<ImageView>(R.id.iv_profile_picture)
        
        val name = sessionManager.getUserName() ?: "Mahasiswa"
        tvName.text = name

        // Load Photo with Glide
        var photoUrl = sessionManager.getUserPhoto()
        if (!photoUrl.isNullOrEmpty()) {
            // Fix URL if it's relative or pointing to wrong IP
            // Don't touch if it's http, https, content://, or file://
            if (!photoUrl.startsWith("http") && !photoUrl.startsWith("content") && !photoUrl.startsWith("file")) {
                val baseUrl = RetrofitClient.BASE_URL.replace("index.php/", "")
                photoUrl = "$baseUrl$photoUrl"
            }
            
            // DEBUG: Show URL
            // Toast.makeText(this, "Load: $photoUrl", Toast.LENGTH_LONG).show()
            
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.cristiano_ronaldo) 
                .error(R.drawable.cristiano_ronaldo) 
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProfile)
        } else {
             ivProfile.setImageResource(R.drawable.cristiano_ronaldo)
        }
    }

    private fun setupMenu() {
        val sessionManager = SessionManager(this)

        // Panggil fungsi untuk setiap item menu
        setupMenuItem(
            view = findViewById(R.id.menu_detail_profile),
            iconResId = R.drawable.ic_profile,
            title = "Detail Profile",
            subtitle = "${sessionManager.getUserNim()} - ${sessionManager.getUserProdi()}"
        ) {
             startActivity(Intent(this, DetailProfileActivity::class.java))
        }

        setupMenuItem(
            view = findViewById(R.id.menu_privacy_account),
            iconResId = R.drawable.ic_settings,
            title = "Privacy Account",
            subtitle = "Edit Account"
        ) {
            startActivity(Intent(this, PrivacyAccountActivity::class.java))
        }

        setupMenuItem(
            view = findViewById(R.id.menu_help_center),
            iconResId = R.drawable.ic_info,
            title = "Help Center",
            subtitle = "Information Details"
        ) {
            startActivity(Intent(this, HelpCenterActivity::class.java))
        }

        setupMenuItem(
            view = findViewById(R.id.menu_privacy_policy),
            iconResId = R.drawable.ic_shield,
            title = "Privacy Policy",
            subtitle = "Information Details"
        ) {
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
            sessionManager.clearSession()
            val intent = Intent(this, LoginOrRegist::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finishAffinity() 
        }
    }

    private fun setupMenuItem(view: View, iconResId: Int, title: String, subtitle: String, onClick: () -> Unit) {
        view.findViewById<ImageView>(R.id.iv_menu_icon).setImageResource(iconResId)
        view.findViewById<TextView>(R.id.tv_menu_title).text = title
        view.findViewById<TextView>(R.id.tv_menu_subtitle).text = subtitle
        view.setOnClickListener { onClick() }
    }

    private fun setupFooter() {
        val footer = findViewById<ConstraintLayout>(R.id.footer_container)
        ViewCompat.setOnApplyWindowInsetsListener(footer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            windowInsets
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavView.selectedItemId = R.id.nav_profil

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
                R.id.nav_profil -> true
                else -> {
                    Toast.makeText(this, "Menu belum tersedia", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
    }
}
