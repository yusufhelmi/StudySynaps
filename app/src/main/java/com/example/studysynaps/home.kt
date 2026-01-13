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
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studysynaps.network.RetrofitClient
import com.example.studysynaps.models.ScheduleItem
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
        
        setupScheduleRecyclerView()
        fetchTodaySchedule()
    }

    override fun onResume() {
        super.onResume()
        // Refresh User Data (Name, Status, Photo) every time we return
        setupUserData()
    }

    private fun setupUserData() {
        val sessionManager = SessionManager(this)
        
        val tvName = findViewById<TextView>(R.id.tv_user_name)
        val tvNim = findViewById<TextView>(R.id.tv_user_id)
        val tvStatus = findViewById<TextView>(R.id.tv_status)
        val ivProfile = findViewById<ImageView>(R.id.iv_profile_pic)
        
        val rawNim = sessionManager.getUserNim() ?: ""
        tvName.text = sessionManager.getUserName() ?: "Mahasiswa"
        
        if (rawNim.length == 8 && rawNim.all { it.isDigit() }) {
            val formattedNim = "${rawNim.substring(0, 2)}.${rawNim.substring(2, 4)}.${rawNim.substring(4)}"
            tvNim.text = formattedNim
        } else {
            tvNim.text = rawNim.ifEmpty { "NIM Tidak Ditemukan" }
        }

        // Status Logic
        val status = sessionManager.getUserStatus() ?: "inactive"
        if (status == "active") {
            tvStatus.text = "Aktif"
            tvStatus.setTextColor(android.graphics.Color.parseColor("#15803D")) // Dark Green
            tvStatus.background.setTint(android.graphics.Color.parseColor("#DCFCE7")) // Light Green
        } else {
            tvStatus.text = "Tidak Aktif"
            tvStatus.setTextColor(android.graphics.Color.parseColor("#B91C1C")) // Dark Red
            tvStatus.background.setTint(android.graphics.Color.parseColor("#FEE2E2")) // Light Red
        }

        // Photo Logic (Glide)
        var photoUrl = sessionManager.getUserPhoto()
        if (!photoUrl.isNullOrEmpty()) {
            // Fix URL if it's relative or pointing to wrong IP
            if (!photoUrl.startsWith("http") && !photoUrl.startsWith("content") && !photoUrl.startsWith("file")) {
                // Relative path case (assets/...)
                val baseUrl = RetrofitClient.BASE_URL.replace("index.php/", "")
                photoUrl = "$baseUrl$photoUrl"
            }
            
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
    
    // Helper to check status
    private fun isUserActive(): Boolean {
        val session = SessionManager(this)
        return session.getUserStatus() == "active"
    }

    private fun setupEdgeToEdge() {
        val contentScrollView: NestedScrollView = findViewById(R.id.content_scroll_view)
        ViewCompat.setOnApplyWindowInsetsListener(contentScrollView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun showInactiveAlert() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Akses Dibatasi")
            .setMessage("Status kemahasiswaan Anda belum aktif. Silakan lakukan pembayaran tagihan semester ini untuk membuka akses fitur ini.")
            .setPositiveButton("Bayar Sekarang") { _, _ ->
                startActivity(Intent(this, PaymentActivity::class.java))
            }
            .setNegativeButton("Tutup", null)
            .show()
    }

    private fun setupMenu() {
        // Menu Clicks
        findViewById<View>(R.id.btn_presensi).setOnClickListener {
            if (isUserActive()) startActivity(Intent(this, PresensiActivity::class.java)) else showInactiveAlert()
        }
        findViewById<View>(R.id.btn_transkrip).setOnClickListener {
            startActivity(Intent(this, TranskripActivity::class.java))
        }
        findViewById<View>(R.id.btn_ktm).setOnClickListener {
            startActivity(Intent(this, KtmActivity::class.java))
        }
        findViewById<View>(R.id.btn_hasil_studi).setOnClickListener {
            startActivity(Intent(this, HasilStudiActivity::class.java))
        }
        findViewById<View>(R.id.btn_pembayaran).setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }

    private fun setupScheduleRecyclerView() {
        val rvToday = findViewById<RecyclerView>(R.id.rv_today_schedule)
        todayAdapter = AdapterJadwal(emptyList())
        rvToday.adapter = todayAdapter
        // Set Horizontal Layout
        rvToday.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchTodaySchedule() {
        val session = SessionManager(this)
        val userId = session.getUserId()

        val tvTitle = findViewById<TextView>(R.id.tv_schedule_title)
        val todayName = SimpleDateFormat("EEEE", Locale("id", "ID")).format(Date())
        tvTitle.text = "Jadwal Hari Ini ($todayName)"

        if (userId == null) return
        
        RetrofitClient.instance.getTodaySchedule(userId, todayName).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>> {
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
                    rvToday.visibility = View.GONE
                    emptyLayout.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<List<ScheduleItem>>>, t: Throwable) {
                findViewById<RecyclerView>(R.id.rv_today_schedule).visibility = View.GONE
                findViewById<View>(R.id.layout_empty_schedule).visibility = View.VISIBLE
            }
        })
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
                    if (isUserActive()) {
                        startActivity(Intent(this, ScanActivity::class.java))
                        overridePendingTransition(0, 0)
                        true
                    } else {
                        showInactiveAlert()
                        false
                    }
                }
                R.id.nav_krs -> {
                    if (isUserActive()) {
                        startActivity(Intent(this, KrsActivity::class.java))
                        overridePendingTransition(0, 0)
                        true
                    } else {
                        showInactiveAlert()
                        false
                    }
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
