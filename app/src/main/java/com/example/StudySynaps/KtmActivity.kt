package com.example.StudySynaps

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
        setContentView(R.layout.activity_ktm)

        setupEdgeToEdge()
        setupCardInteraction()
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
        }
    }
}
