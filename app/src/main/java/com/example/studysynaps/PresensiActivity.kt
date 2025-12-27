package com.example.studysynaps

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PresensiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_presensi)

        setupEdgeToEdge()
        setupRecyclerView()
        setupBackButton()
    }

    private fun setupEdgeToEdge() {
        val headerContainer = findViewById<android.view.View>(R.id.header_container)
        ViewCompat.setOnApplyWindowInsetsListener(headerContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupRecyclerView() {
        val rvPresensi = findViewById<RecyclerView>(R.id.rv_presensi)
        rvPresensi.layoutManager = LinearLayoutManager(this)

        // Dummy data based on the user's request/image
        val presensiList = listOf(
            PresensiItem("Bahasa Pemrograman II", "SISI06 2 SKS", 8),
            PresensiItem("Struktur Data", "SISI06 2 SKS", 6),
            PresensiItem("Manajemen Finansial", "SISI06 2 SKS", 6),
            PresensiItem("Success Skill", "SISI06 2 SKS", 6),
            PresensiItem("Sistem Basis Data", "SISI06 2 SKS", 7)
        )

        val adapter = PresensiAdapter(presensiList)
        rvPresensi.adapter = adapter
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }
}
