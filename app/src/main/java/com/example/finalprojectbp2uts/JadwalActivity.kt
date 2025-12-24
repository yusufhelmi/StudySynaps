package com.example.finalprojectbp2uts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.ChipGroup

class JadwalActivity : AppCompatActivity() {

    private lateinit var jadwalAdapter: JadwalAdapter
    private val allJadwal = mapOf(
        "Senin" to listOf(
            Jadwal("Ninik Tri Hartanti, S.Kom., M.Kom.", "STRUKTUR DATA", "10.40 - 12.20", "L 2.4.1", "Praktikum", Color.YELLOW),
            Jadwal("Irton, S.E., M.Si.", "MANAJEMEN KEUANGAN", "13.20 - 15.00", "05.02.06", "Teori", Color.BLUE),
            Jadwal("Wiwiek Widayani, S.Kom., M.Kom.", "SISTEM MANAJEMEN BASIS DATA", "15.30 - 17.10", "L 7.4.2", "Praktikum", Color.YELLOW)
        ),
        "Selasa" to listOf(
            Jadwal("Dosen A", "MATKUL A", "08.00 - 09.40", "R. 1", "Teori", Color.BLUE)
        ),
        "Rabu" to emptyList(), // Tidak ada jadwal
        "Kamis" to listOf(
            Jadwal("Dosen B", "MATKUL B", "13.00 - 14.40", "R. 2", "Praktikum", Color.YELLOW),
            Jadwal("Dosen C", "MATKUL C", "15.00 - 16.40", "R. 3", "Teori", Color.BLUE)
        ),
        "Jumat" to listOf(
            Jadwal("Dosen D", "MATKUL D", "10.00 - 11.40", "R. 4", "Teori", Color.BLUE)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jadwal)

        setupEdgeToEdge()
        setupFooter()
        setupRecyclerView()
        setupChipGroup()
        setupBackButton()

        updateJadwalForDay("Senin")
    }

    private fun setupEdgeToEdge() {
        val contentContainer = findViewById<LinearLayout>(R.id.content_container)
        ViewCompat.setOnApplyWindowInsetsListener(contentContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupRecyclerView() {
        val rvJadwal = findViewById<RecyclerView>(R.id.rv_jadwal)
        jadwalAdapter = JadwalAdapter(emptyList())
        rvJadwal.adapter = jadwalAdapter
    }

    private fun setupChipGroup() {
        val chipGroup = findViewById<ChipGroup>(R.id.chip_group_days)
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_senin -> updateJadwalForDay("Senin")
                R.id.chip_selasa -> updateJadwalForDay("Selasa")
                R.id.chip_rabu -> updateJadwalForDay("Rabu")
                R.id.chip_kamis -> updateJadwalForDay("Kamis")
                R.id.chip_jumat -> updateJadwalForDay("Jumat")
            }
        }
    }

    private fun updateJadwalForDay(day: String) {
        val jadwalForDay = allJadwal[day] ?: emptyList()
        jadwalAdapter.updateData(jadwalForDay)
    }



    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
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
        bottomNavView.selectedItemId = R.id.nav_jadwal

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, home::class.java))
                    true
                }
                R.id.nav_jadwal -> true
                R.id.nav_krs -> {
                    startActivity(Intent(this, KrsActivity::class.java))
                    true
                }
                R.id.nav_scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    true
                }
                // Baris 'kotlin' yang salah sudah dihapus dari sini
                R.id.nav_profil -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
