package com.example.studysynaps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class IntroScreen1 : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var dot1: View
    private lateinit var dot2: View
    private lateinit var dot3: View
    private lateinit var btnLanjutkan: Button
    private lateinit var btnLewati: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_screen1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
        dot1 = findViewById(R.id.dot1)
        dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3)
        btnLanjutkan = findViewById(R.id.buttonLanjut)
        btnLewati = findViewById(R.id.textViewLewati)

        val introItems = listOf(
            IntroItem(R.drawable.ic_intro_1, "StudySynaps", "Kelola tugas dengan mudah, dan dengan sekali klik semua yang Anda butuhkan untuk kelas akan terintegrasi."),
            IntroItem(R.drawable.ic_intro_2, "Fitur Modern", "Tukar peran Anda dengan lancar, dan dengan sekali klik Anda akan berada di pusat aktivitas akademik."),
            IntroItem(R.drawable.ic_intro_3, "User Family", "Akses semua data akademik Anda dengan mudah, dan dengan sekali klik dasbor Anda siap kapan pun Anda membutuhkannya."),
            IntroItem(0, "", "") // Dummy item for swipe-to-finish
        )

        val adapter = IntroAdapter(introItems)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == introItems.size - 1) {
                    // This is the dummy item
                    goToLogin()
                } else {
                    updateDots(position)
                    
                    // Hide "Lewati" if on the last real item (index 2)
                    if (position == introItems.size - 2) {
                        btnLewati.visibility = View.INVISIBLE
                        btnLanjutkan.text = "Mulai" // Optional: Change button text to "Start" or keep "Lanjutkan"
                    } else {
                        btnLewati.visibility = View.VISIBLE
                        btnLanjutkan.text = "Lanjutkan"
                    }
                }
            }
        })

        btnLanjutkan.setOnClickListener {
            if (viewPager.currentItem < introItems.size - 2) {
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                goToLogin()
            }
        }

        btnLewati.setOnClickListener {
            goToLogin()
        }
    }

    private fun updateDots(position: Int) {
        dot1.setBackgroundResource(if (position == 0) R.drawable.circle else R.drawable.circlebolong)
        dot2.setBackgroundResource(if (position == 1) R.drawable.circle else R.drawable.circlebolong)
        dot3.setBackgroundResource(if (position == 2) R.drawable.circle else R.drawable.circlebolong)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginOrRegist::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}