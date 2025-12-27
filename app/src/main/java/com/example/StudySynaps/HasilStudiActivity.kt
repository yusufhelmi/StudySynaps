package com.example.StudySynaps

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HasilStudiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hasil_studi)

        setupEdgeToEdge()
        setupBackButton()
        setupRecyclerView()
    }

    private fun setupEdgeToEdge() {
        val headerContainer = findViewById<android.view.View>(R.id.header_container)
        ViewCompat.setOnApplyWindowInsetsListener(headerContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_hasil_studi)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            HasilStudiItem("Bahasa Pemrograman I", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Bahasa Pemrograman II", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Struktur Data", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Manajemen Finansial", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Succes Skill", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Perancangan Web Lanjut", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Sistem Basis Data", "S1SI06 2 SKS", "A"),
            HasilStudiItem("Keamanan Sistem", "S1SI06 2 SKS", "A")
        )

        recyclerView.adapter = HasilStudiAdapter(items)
    }
}
