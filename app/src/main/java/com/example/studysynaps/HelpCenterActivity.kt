package com.example.studysynaps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HelpCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnContact = findViewById<Button>(R.id.btn_contact_us)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnContact.setOnClickListener {
            // Intent to WhatsApp
            val phoneNumber = "6281234567890" // Dummy Number
            val message = "Halo Admin StudySynaps, saya butuh bantuan."
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=${java.net.URLEncoder.encode(message, "UTF-8")}"
            
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (e: Exception) {
                // Fallback if no browser/WA
            }
        }
    }
}
