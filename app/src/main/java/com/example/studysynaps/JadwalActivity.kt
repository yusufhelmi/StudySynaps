package com.example.studysynaps

import android.content.Intent
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
import com.example.studysynaps.network.RetrofitClient
import com.example.studysynaps.models.ScheduleItem
import com.example.studysynaps.models.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalActivity : AppCompatActivity() {

    private lateinit var jadwalAdapter: AdapterJadwal
    private var allSchedules: List<ScheduleItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jadwal)

        setupEdgeToEdge()
        setupFooter()
        setupRecyclerView()
        setupChipGroup()
        setupBackButton()

        fetchSchedule()
    }

    private fun fetchSchedule() {
        val session = SessionManager(this)
        val userId = session.getUserId()

        if (userId == null) {
            Toast.makeText(this, "Sesi habis, login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.getMySchedule(userId).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>,
                response: Response<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    allSchedules = response.body()?.data ?: emptyList()
                    // Default hari Senin
                    filterScheduleByDay("Senin")
                    // Success toast removed
                } else {
                    Toast.makeText(this@JadwalActivity, "Belum ada jadwal (Isi KRS dulu)", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>, t: Throwable) {
                Toast.makeText(this@JadwalActivity, "Error koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
        jadwalAdapter = AdapterJadwal(emptyList())
        rvJadwal.adapter = jadwalAdapter
    }

    private fun setupChipGroup() {
        val chipGroup = findViewById<ChipGroup>(R.id.chip_group_days)
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_senin -> filterScheduleByDay("Senin")
                R.id.chip_selasa -> filterScheduleByDay("Selasa")
                R.id.chip_rabu -> filterScheduleByDay("Rabu")
                R.id.chip_kamis -> filterScheduleByDay("Kamis")
                R.id.chip_jumat -> filterScheduleByDay("Jumat")
            }
        }
    }

    private fun filterScheduleByDay(day: String) {
        val filteredList = allSchedules.filter { it.day.equals(day, ignoreCase = true) }
        jadwalAdapter.updateData(filteredList)
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
                    overridePendingTransition(0,0)
                }
                R.id.nav_jadwal -> true
                R.id.nav_krs -> {
                    startActivity(Intent(this, KrsActivity::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_scan -> {
                    startActivity(Intent(this, ScanActivity::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_profil -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)
                }
            }
            true
        }
    }
}
