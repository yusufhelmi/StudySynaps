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
        // Set Status Bar Icons to Black (Light Background)
        androidx.core.view.WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
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
        
        // Fetch Real Data
        val session = com.example.studysynaps.models.SessionManager(this)
        val userId = session.getUserId()

        if (userId != null) {
            com.example.studysynaps.network.RetrofitClient.instance.getAttendanceSummary(userId)
                .enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<List<PresensiItem>>> {
                    override fun onResponse(
                        call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<PresensiItem>>>,
                        response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<List<PresensiItem>>>
                    ) {
                        if (response.isSuccessful && response.body()?.status == true) {
                            val list = response.body()?.data ?: emptyList()
                            rvPresensi.adapter = PresensiAdapter(list)
                        } else {
                            android.widget.Toast.makeText(this@PresensiActivity, "Gagal memuat presensi", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<PresensiItem>>>,
                        t: Throwable
                    ) {
                        android.widget.Toast.makeText(this@PresensiActivity, "Error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }
}
