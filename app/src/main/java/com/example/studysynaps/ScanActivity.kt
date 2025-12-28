package com.example.studysynaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ScanActivity : AppCompatActivity() {

    // --- BAGIAN INI KITA PINDAHKAN KE SCANQRFRAGMENT ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        // Cari komponen UI
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val btnBack: ImageButton = findViewById(R.id.btn_back)

        // Setup Tombol Kembali
        btnBack.setOnClickListener {
            finish() // Cukup panggil finish() untuk kembali
            overridePendingTransition(0, 0)
        }

        // Setup ViewPager Adapter
        viewPager.adapter = ScanViewPagerAdapter(this)

        // Hubungkan TabLayout dengan ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Scan QR" else "Kode"
        }.attach()

        // Pindahkan logika permintaan izin ke dalam fragment yang membutuhkan kamera
    }
}
