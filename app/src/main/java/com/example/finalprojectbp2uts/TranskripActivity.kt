package com.example.finalprojectbp2uts

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class TranskripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transkrip)

        setupEdgeToEdge()
        setupRecyclerView()
        setupCircularIndicator()
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

    private fun setupCircularIndicator() {
        // Example IPK calculation representation
        val ipk = 3.76
        val progress = ((ipk / 4.0) * 100).toInt()
        val indicator = findViewById<CircularProgressIndicator>(R.id.progress_ipk)
        indicator.progress = progress
    }

    private fun setupRecyclerView() {
        val rvTranskrip = findViewById<RecyclerView>(R.id.rv_transkrip)
        rvTranskrip.layoutManager = LinearLayoutManager(this)

        // Dummy data based on user image
        // Colors: A=#1A237E (Dark Blue), B=#2E7D32 (Green), C=#AD8E00 (Gold/Ochre)
        val colorA = Color.parseColor("#1A237E")
        val colorB = Color.parseColor("#1B5E20") // Darker green
        val colorC = Color.parseColor("#F9A825") // Generic gold

        val transkripList = listOf(
            TranskripItem("Bahasa Pemrograman I", "SISI06 2 SKS", "A", colorA),
            TranskripItem("Bahasa Pemrograman II", "SISI06 2 SKS", "B", colorB),
            TranskripItem("Manajemen Strategik", "SISI06 2 SKS", "A", colorA),
            TranskripItem("Pemrograman Web Lanjut", "SISI06 2 SKS", "A", colorA),
            TranskripItem("Struktur Data", "SISI06 2 SKS", "C", colorC),
            TranskripItem("Basis Data", "SISI06 2 SKS", "A", colorA),
            TranskripItem("Jaringan Komputer", "SISI06 2 SKS", "B", colorB)
        )

        val adapter = TranskripAdapter(transkripList)
        rvTranskrip.adapter = adapter
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }
}
