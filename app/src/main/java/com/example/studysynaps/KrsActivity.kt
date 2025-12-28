package com.example.studysynaps

import android.content.Intent
import android.os.Bundle    import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class KrsActivity : AppCompatActivity() {

    private lateinit var krsAdapter: KrsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_krs)

        setupEdgeToEdge()
        setupRecyclerView()
        setupFooter()

        findViewById<android.widget.ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val rvKrs: RecyclerView = findViewById(R.id.rv_krs)
        val data = createDummyData()
        krsAdapter = KrsAdapter(data)
        rvKrs.adapter = krsAdapter
    }

    private fun createDummyData(): List<KrsItem> {
        return listOf(
            KrsItem(1, "Bahasa Pemrograman II", "Stevi Ema Wijayanti, M.Kom", 2, true),
            KrsItem(2, "Struktur Data", "Ninik Tri Hartanti, S.Kom., M.Kom.", 3),
            KrsItem(3, "Manajemen Keuangan", "Irton, S.E., M.Si.", 3),
            KrsItem(4, "Basis Data Lanjut", "Wiwiek Widayani, S.Kom., M.Kom.", 2),
            KrsItem(5, "Jaringan Komputer", "Dosen Jarkom", 3),
            KrsItem(6, "Sistem Operasi", "Dosen SO", 3)
        )
    }

    private fun setupEdgeToEdge() {
        val contentContainer: LinearLayout = findViewById(R.id.content_container)
        ViewCompat.setOnApplyWindowInsetsListener(contentContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupFooter() {
        val footer = findViewById<ConstraintLayout>(R.id.footer_container)
        ViewCompat.setOnApplyWindowInsetsListener(footer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            windowInsets
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavView.selectedItemId = R.id.nav_krs

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, home::class.java))
                R.id.nav_jadwal -> startActivity(Intent(this, JadwalActivity::class.java))
                R.id.nav_scan -> startActivity(Intent(this, ScanActivity::class.java))
                R.id.nav_profil -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            true
        }
    }
}
    