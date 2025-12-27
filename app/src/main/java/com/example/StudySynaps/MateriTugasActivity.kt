package com.example.StudySynaps

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
    private lateinit var adapter: MateriTugasAdapter
    private lateinit var btnMateri: AppCompatButton
    private lateinit var btnTugas: AppCompatButton

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
    }

    private fun setupRecyclerView() {
        rvMateriTugas.layoutManager = LinearLayoutManager(this)
        adapter = MateriTugasAdapter(emptyList())
        rvMateriTugas.adapter = adapter
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
        val materiList = listOf(
            MateriTugasItem("Fungsi Array dan Pointer", "", "Uplode 15 Oktober 2025", ItemType.MATERI),
            MateriTugasItem("Penerapan Pointer", "", "Uplode 15 Oktober 2025", ItemType.MATERI),
            MateriTugasItem("Fungsi Array dan Pointer", "", "Uplode 15 Oktober 2025", ItemType.MATERI),
            MateriTugasItem("Fungsi Array dan Pointer", "", "Uplode 15 Oktober 2025", ItemType.MATERI)
        )
        adapter.updateData(materiList)
    }

    private fun showTugas() {
        // Update UI
        btnTugas.setBackgroundResource(R.drawable.toggle_bg_active)
        btnTugas.setTextColor(Color.WHITE)

        btnMateri.setBackgroundResource(R.drawable.toggle_bg_inactive)
        btnMateri.setTextColor(Color.parseColor("#FFC107"))

        // Update Data
        val tugasList = listOf(
            MateriTugasItem("Tugas praktikum 1", "Tenggat 15 Oktober 2025", "Uplode 15 Oktober 2025", ItemType.TUGAS),
            MateriTugasItem("Tugas 2 Array", "Tenggat 15 Oktober 2025", "Uplode 15 Oktober 2025", ItemType.TUGAS),
            MateriTugasItem("Tugas 3 Array", "Tenggat 15 Oktober 2025", "Uplode 15 Oktober 2025", ItemType.TUGAS),
            MateriTugasItem("Tugas Array dan Pointer", "Tenggat 25 Oktober 2025", "Uplode 15 Oktober 2025", ItemType.TUGAS)
        )
        adapter.updateData(tugasList)
    }
}
