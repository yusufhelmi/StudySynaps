package com.example.studysynaps

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.card.MaterialCardView

class KtmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Set Status Bar Icons to White (Dark Background)
        androidx.core.view.WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        setContentView(R.layout.activity_ktm)

        setupEdgeToEdge()
        setupCardInteraction()
        setupBackButton()
        setupKtmData()
    }

    private fun setupKtmData() {
        val session = com.example.studysynaps.models.SessionManager(this)
        val tvSmall = findViewById<android.widget.TextView>(R.id.tv_ktm_details_small)
        val tvLarge = findViewById<android.widget.TextView>(R.id.tv_ktm_details_large)
        val ivSmall = findViewById<android.widget.ImageView>(R.id.iv_photo)
        val ivLarge = findViewById<android.widget.ImageView>(R.id.iv_photo_full)

        val rawNim = session.getUserNim() ?: ""
        val formattedNim = if (rawNim.length == 8 && rawNim.all { it.isDigit() }) {
             "${rawNim.substring(0, 2)}.${rawNim.substring(2, 4)}.${rawNim.substring(4)}"
        } else {
             rawNim
        }

        val name = session.getUserName()?.uppercase() ?: "MAHASISWA"
        val prodi = session.getUserProdi()?.uppercase() ?: "SISTEM INFORMASI"
        // Status typically "Active" -> "MAHASISWA"
        val statusLabel = "PELAJAR / MAHASISWA"

        val details = "NAMA :\n$name\nNOMOR INDUK MAHASISWA / NIM :\n$formattedNim\nPROGRAM STUDI :\n$prodi\nSTATUS :\n$statusLabel"

        tvSmall.text = details
        tvLarge.text = details

        // Load Photo
        var photoUrl = session.getUserPhoto()
        if (!photoUrl.isNullOrEmpty()) {
            if (!photoUrl.startsWith("http") && !photoUrl.startsWith("content") && !photoUrl.startsWith("file")) {
                val baseUrl = com.example.studysynaps.network.RetrofitClient.BASE_URL.replace("index.php/", "")
                photoUrl = "$baseUrl$photoUrl"
            }
            
            val requestManager = com.bumptech.glide.Glide.with(this)
            requestManager.load(photoUrl)
                .placeholder(R.drawable.ic_profile_24)
                .error(R.drawable.ic_profile_24)
                .signature(com.bumptech.glide.signature.ObjectKey(System.currentTimeMillis()))
                .into(ivSmall)
            
            requestManager.load(photoUrl)
                .placeholder(R.drawable.ic_profile_24)
                .error(R.drawable.ic_profile_24)
                .signature(com.bumptech.glide.signature.ObjectKey(System.currentTimeMillis()))
                .into(ivLarge)
        }

        // Generate QR Code
        val ivQr = findViewById<android.widget.ImageView>(R.id.iv_qr_code)
        // Ensure tint is null
        ivQr.imageTintList = null
        try {
            // Using rawNim for the value, or formatted if preferred. User said "isinya adalah nim".
            // rawNim is safer standard.
            val qrContent = if (rawNim.isNotEmpty()) rawNim else "STUDYSYNAPS"
            val qrBitmap = generateQRCode(qrContent)
            ivQr.setImageBitmap(qrBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupEdgeToEdge() {
        val headerContainer = findViewById<android.view.View>(R.id.header_container)
        ViewCompat.setOnApplyWindowInsetsListener(headerContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupCardInteraction() {
        val cardKtm = findViewById<MaterialCardView>(R.id.card_ktm)
        val overlay = findViewById<FrameLayout>(R.id.overlay_full_card)

        // Show overlay on card click
        cardKtm.setOnClickListener {
            overlay.visibility = View.VISIBLE
        }

        // Hide overlay on overlay click
        overlay.setOnClickListener {
            overlay.visibility = View.GONE
        }
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
            finish()
        }
    }

    private fun generateQRCode(text: String): android.graphics.Bitmap {
        val width = 500
        val height = 500
        val bitMatrix = com.google.zxing.MultiFormatWriter().encode(
            text, com.google.zxing.BarcodeFormat.QR_CODE, width, height
        )
        val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                 // 0xFF000000 is Black, 0xFFFFFFFF is White
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }
}
