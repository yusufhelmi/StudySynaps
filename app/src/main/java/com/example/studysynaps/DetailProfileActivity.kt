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
        tvNim.text = sessionManager.getUserNim() ?: "-"
        tvProdi.text = sessionManager.getUserProdi() ?: "-"

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
                .into(ivPhoto)
        }

        // Back Button
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
