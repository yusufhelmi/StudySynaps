package com.example.studysynaps

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.network.RetrofitClient

class DetailProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_profile)

        val sessionManager = SessionManager(this)

        // Init Views
        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvNim = findViewById<TextView>(R.id.tv_detail_nim)
        val tvProdi = findViewById<TextView>(R.id.tv_detail_prodi)
        val ivPhoto = findViewById<ImageView>(R.id.iv_detail_profile)
        val btnBack = findViewById<ImageView>(R.id.btn_back)

        // Set Data
        tvName.text = sessionManager.getUserName() ?: "Mahasiswa"
        
        // Format NIM xx.xx.xxxx
        val rawNim = sessionManager.getUserNim() ?: ""
        if (rawNim.length == 8 && rawNim.all { it.isDigit() }) {
             tvNim.text = "${rawNim.substring(0, 2)}.${rawNim.substring(2, 4)}.${rawNim.substring(4)}"
        } else {
             tvNim.text = rawNim.ifEmpty { "-" }
        }
        
        tvProdi.text = sessionManager.getUserProdi() ?: "-"

        // Fetch SKS
        fetchTotalSks(sessionManager.getUserId(), findViewById(R.id.tv_detail_sks))

        // Photo with Glide
        var photoUrl = sessionManager.getUserPhoto()
        if (!photoUrl.isNullOrEmpty()) {
            if (!photoUrl.startsWith("http") && !photoUrl.startsWith("content") && !photoUrl.startsWith("file")) {
                val baseUrl = RetrofitClient.BASE_URL.replace("index.php/", "")
                photoUrl = "$baseUrl$photoUrl"
            }

            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.cristiano_ronaldo)
                .error(R.drawable.cristiano_ronaldo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(com.bumptech.glide.signature.ObjectKey(System.currentTimeMillis()))
                .into(ivPhoto)
        }

        // Back Button
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    private fun fetchTotalSks(userId: String?, tvSks: TextView) {
        if (userId == null) return

        RetrofitClient.instance.getCourses(userId).enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Course>>> {
            override fun onResponse(
                call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Course>>>,
                response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Course>>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val courses = response.body()?.data ?: emptyList()
                    val totalSks = courses.filter { it.isTaken == "1" }.sumOf { it.sks }
                    tvSks.text = "$totalSks SKS"
                }
            }
            override fun onFailure(call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<List<com.example.studysynaps.models.Course>>>, t: Throwable) {
                // Ignore error, keep default or "-"
            }
        })
    }
}
