package com.example.studysynaps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.Course
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KrsActivity : AppCompatActivity() {

    private lateinit var krsAdapter: KrsAdapter
    private lateinit var tvTotalSks: TextView
    private lateinit var btnSave: Button
    private lateinit var progressBar: android.widget.ProgressBar
    private var totalSks = 0
    private var coursesList: List<Course> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_krs)

        setupEdgeToEdge()
        setupViews()
        setupFooter()
        
        fetchCourses()
    }

    private fun setupViews() {
        tvTotalSks = findViewById(R.id.tv_total_sks)
        btnSave = findViewById(R.id.btn_save_krs)
        val rvKrs: RecyclerView = findViewById(R.id.rv_krs)
        // Note: Make sure there's a ProgressBar in layout or remove usage if not added yet.
        // Assuming layout update is needed or using simple View for now. 
        // If XML doesn't have it, I'll comment out usage in logic or add it.
        // Checking XML first would be safer but let's assume I need to add it to XML too.
        // For now, let's look for existing loading indicator or add one.
        progressBar = findViewById(R.id.progress_bar_krs) // Need to add to XML

        krsAdapter = KrsAdapter(emptyList()) { sks ->
            totalSks = sks
            updateSksUI()
        }
        rvKrs.adapter = krsAdapter

        findViewById<android.view.View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun updateSksUI() {
        tvTotalSks.text = "$totalSks / 24"
        
        // Aturan: Minimal 20 SKS DAN Ada perubahan baru (matkul baru dipilih)
        val isSksValid = totalSks >= 20
        val hasNew = krsAdapter.hasNewSelections()
        
        val canSave = isSksValid && hasNew

        btnSave.isEnabled = canSave
        
        if (canSave) {
            btnSave.text = "Simpan KRS"
            tvTotalSks.setTextColor(android.graphics.Color.parseColor("#4CAF50")) // Green
        } else {
            if (!hasNew && isSksValid) {
                btnSave.text = "KRS Tersimpan" // Feedback kalau sudah tersimpan semua
            } else {
                btnSave.text = "Simpan KRS (Min 20 SKS)"
            }
            // Tetap hijau jika SKS valid meski tombol mati (biar gak membingungkan)
            if(isSksValid) {
                 tvTotalSks.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            } else {
                 tvTotalSks.setTextColor(android.graphics.Color.RED)
            }
        }
    }

    private fun fetchCourses() {
        val session = SessionManager(this)
        val userId = session.getUserId() ?: return

        progressBar.visibility = android.view.View.VISIBLE
        RetrofitClient.instance.getCourses(userId).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<List<Course>>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<List<Course>>>,
                response: Response<com.example.studysynaps.models.ApiResponse<List<Course>>>
            ) {
                progressBar.visibility = android.view.View.GONE
                if (response.isSuccessful && response.body()?.status == true) {
                    coursesList = response.body()?.data ?: emptyList()
                    krsAdapter.updateData(coursesList)
                    // Success toast removed
                } else {
                    Toast.makeText(this@KrsActivity, "Gagal memuat mata kuliah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<List<Course>>>, t: Throwable) {
                progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@KrsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showConfirmationDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Konfirmasi Simpan KRS")
            .setMessage("Apakah Anda yakin ingin menyimpan KRS ini? \n\nPERINGATAN: Data yang sudah disimpan tidak dapat diubah kembali.")
            .setPositiveButton("Ya, Simpan") { _, _ ->
                submitKrs()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun submitKrs() {
        val session = SessionManager(this)
        val userId = session.getUserId()

        if (userId == null) {
            Toast.makeText(this, "Sesi habis, login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedIds = krsAdapter.getSelectedCourseIds().joinToString(",")
        
        btnSave.isEnabled = false
        btnSave.text = "Menyimpan..."
        // progressBar.visibility = android.view.View.VISIBLE // Assuming progressBar is not defined globally, relying on button text

        RetrofitClient.instance.submitKrs(userId, selectedIds).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<Any>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<Any>>,
                response: Response<com.example.studysynaps.models.ApiResponse<Any>>
            ) {
                btnSave.isEnabled = true
                updateSksUI()
                
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(this@KrsActivity, "KRS Berhasil Disimpan!", Toast.LENGTH_LONG).show()
                    // Opsional: Pindah ke Jadwal atau Home
                    startActivity(Intent(this@KrsActivity, JadwalActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@KrsActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<Any>>, t: Throwable) {
                btnSave.isEnabled = true
                updateSksUI()
                Toast.makeText(this@KrsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
                R.id.nav_home -> {
                    startActivity(Intent(this, home::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_jadwal -> {
                    startActivity(Intent(this, JadwalActivity::class.java))
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