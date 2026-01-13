package com.example.studysynaps

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class MateriTugasActivity : AppCompatActivity() {

    private lateinit var rvMateriTugas: RecyclerView
    private lateinit var adapterTugas: AdapterTugas // Adapter Tugas Baru
    private lateinit var adapterMateri: AdapterMateri // Adapter Materi
    private lateinit var btnMateri: AppCompatButton
    private lateinit var btnTugas: AppCompatButton
    private var courseName: String = "" // Default empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_materi_tugas)

        setupEdgeToEdge()
        setupViews()
        setupRecyclerView()
        setupListeners()
        
        // Initial state: Materi
        showMateri()
    }

    private fun setupEdgeToEdge() {
        val headerContainer = findViewById<android.view.View>(R.id.header_container)
        ViewCompat.setOnApplyWindowInsetsListener(headerContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupViews() {
        rvMateriTugas = findViewById(R.id.rv_materi_tugas)
        btnMateri = findViewById(R.id.btn_materi)
        btnTugas = findViewById(R.id.btn_tugas)
        
        // Populate Data from Intent
        courseName = intent.getStringExtra("COURSE_NAME") ?: ""
        val lecturerName = intent.getStringExtra("LECTURER_NAME") ?: "Dosen Pengampu"
        
        val displayCourse = if(courseName.isNotEmpty()) courseName else "Mata Kuliah"
        
        findViewById<android.widget.TextView>(R.id.tv_course_name).text = displayCourse
        findViewById<android.widget.TextView>(R.id.tv_lecturer_name).text = lecturerName
    }

    private fun setupRecyclerView() {
        rvMateriTugas.layoutManager = LinearLayoutManager(this)
        adapterTugas = AdapterTugas(emptyList())
        adapterMateri = AdapterMateri(emptyList())
        // Default show materi, so set adapterMateri later in showMateri()
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        btnMateri.setOnClickListener {
            showMateri()
        }

        btnTugas.setOnClickListener {
            showTugas()
        }
    }

    private fun showMateri() {
        // Update UI
        btnMateri.setBackgroundResource(R.drawable.toggle_bg_active)
        btnMateri.setTextColor(Color.WHITE)
        
        btnTugas.setBackgroundResource(R.drawable.toggle_bg_inactive)
        btnTugas.setTextColor(Color.parseColor("#FFC107")) // Yellow/Gold

        // Update Data
        // Update Data (Ambil dari API)
        rvMateriTugas.adapter = adapterMateri // Switch Adapter
        
        com.example.studysynaps.network.RetrofitClient.instance.getMaterials(courseName).enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Material>>> {
            override fun onResponse(
                call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Material>>>,
                response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Material>>>
            ) {
                if (response.isSuccessful) {
                    val materials = response.body()?.data ?: emptyList()
                    adapterMateri.updateData(materials)
                } else {
                    // android.widget.Toast.makeText(this@MateriTugasActivity, "Gagal mengambil materi", android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Material>>>,
                t: Throwable
            ) {
                android.widget.Toast.makeText(this@MateriTugasActivity, "Error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showTugas() {
        // Update UI
        btnTugas.setBackgroundResource(R.drawable.toggle_bg_active)
        btnTugas.setTextColor(Color.WHITE)

        btnMateri.setBackgroundResource(R.drawable.toggle_bg_inactive)
        btnMateri.setTextColor(Color.parseColor("#FFC107"))

        // Update Data
        rvMateriTugas.adapter = adapterTugas // Switch adapter

        val session = com.example.studysynaps.models.SessionManager(this)
        val userId = session.getUserId() ?: ""
        
        com.example.studysynaps.network.RetrofitClient.instance.getAssignments(courseName, userId).enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Assignment>>> {
            override fun onResponse(
                call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Assignment>>>,
                response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Assignment>>>
            ) {
                if (response.isSuccessful) {
                    val assignments = response.body()?.data ?: emptyList()
                    adapterTugas.updateData(assignments)
                } else {
                    // android.widget.Toast.makeText(this@MateriTugasActivity, "Gagal mengambil tugas", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Assignment>>>, t: Throwable) {
                android.widget.Toast.makeText(this@MateriTugasActivity, "Error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        })
    }
}
