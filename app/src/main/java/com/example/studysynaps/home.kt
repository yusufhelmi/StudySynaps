package com.example.studysynaps

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
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.network.RetrofitClient // Import Retrofit
import com.example.studysynaps.models.ScheduleItem // Import ScheduleItem
import com.example.studysynaps.models.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class home : AppCompatActivity() {

    private lateinit var todayAdapter: AdapterJadwal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        setContentView(R.layout.activity_home)

        setupEdgeToEdge()
        setupFooter()
        setupMenu()
        setupUserData()
        
        setupScheduleRecyclerView()
        fetchTodaySchedule()
    }

    private fun setupScheduleRecyclerView() {
        val rvToday = findViewById<RecyclerView>(R.id.rv_today_schedule)
        todayAdapter = AdapterJadwal(emptyList())
        rvToday.adapter = todayAdapter
    }

    private fun fetchTodaySchedule() {
        val session = SessionManager(this)
        val userId = session.getUserId()

        // Setup Tanggal Hari Ini di Title, misal: "Jadwal Hari Ini (Senin)"
        val tvTitle = findViewById<TextView>(R.id.tv_schedule_title)
        val todayName = SimpleDateFormat("EEEE", Locale("id", "ID")).format(Date())
        tvTitle.text = "Jadwal Hari Ini ($todayName)"

        if (userId == null) return

        RetrofitClient.instance.getTodaySchedule(userId).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>,
                response: Response<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>
            ) {
                val rvToday = findViewById<RecyclerView>(R.id.rv_today_schedule)
                val emptyLayout = findViewById<View>(R.id.layout_empty_schedule)

                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()?.data ?: emptyList()
                    if (data.isNotEmpty()) {
                        todayAdapter.updateData(data)
                        rvToday.visibility = View.VISIBLE
                        emptyLayout.visibility = View.GONE
                    } else {
                        rvToday.visibility = View.GONE
                        emptyLayout.visibility = View.VISIBLE
                    }
                } else {
                    // Jika gagal/kosong dari API
                    rvToday.visibility = View.GONE
                    emptyLayout.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>, t: Throwable) {
                // Error connection also shows empty state
                findViewById<RecyclerView>(R.id.rv_today_schedule).visibility = View.GONE
                findViewById<View>(R.id.layout_empty_schedule).visibility = View.VISIBLE
            }
        })
    }

    private fun setupUserData() {
        val sessionManager = SessionManager(this)
        
        val tvName = findViewById<TextView>(R.id.tv_user_name)
        val tvNim = findViewById<TextView>(R.id.tv_user_id)
        
        val rawNim = sessionManager.getUserNim() ?: ""
        tvName.text = sessionManager.getUserName() ?: "Mahasiswa"
        
        if (rawNim.length == 8 && rawNim.all { it.isDigit() }) {
            val formattedNim = "${rawNim.substring(0, 2)}.${rawNim.substring(2, 4)}.${rawNim.substring(4)}"
            tvNim.text = formattedNim
        } else {
            tvNim.text = rawNim.ifEmpty { "NIM Tidak Ditemukan" }
        }
    }

    private fun setupEdgeToEdge() {
        val contentScrollView: NestedScrollView = findViewById(R.id.content_scroll_view)
        ViewCompat.setOnApplyWindowInsetsListener(contentScrollView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

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
    }

    private fun setupFooter() {
        val footer = findViewById<ConstraintLayout>(R.id.footer_container)
        ViewCompat.setOnApplyWindowInsetsListener(footer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(bottom = insets.bottom)
            windowInsets
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavView.selectedItemId = R.id.nav_home

        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
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
