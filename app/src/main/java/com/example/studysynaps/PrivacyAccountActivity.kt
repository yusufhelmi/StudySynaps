package com.example.studysynaps

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class PrivacyAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_account)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val cardUpdatePhoto = findViewById<MaterialCardView>(R.id.card_update_photo)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        cardUpdatePhoto.setOnClickListener {
            // Navigate to Update UpdatePhotoActivity
            val intent = Intent(this, UpdatePhotoActivity::class.java)
            startActivity(intent)
        }
    }
}
