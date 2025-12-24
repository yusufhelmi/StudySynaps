// In app/src/main/java/com/example/finalprojectbp2uts/ScanViewPagerAdapter.kt

package com.example.finalprojectbp2uts

import androidx.fragment.app.Fragment
// import androidx.fragment.app.Fragment  <-- Duplikat sudah dihapus
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScanViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // Kurung kurawal dipindahkan ke baris baru agar sintaksisnya benar
    override fun getItemCount(): Int {
        // Jumlah tab yang Anda miliki adalah 2
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Mengembalikan fragment yang sesuai untuk setiap posisi tab
        return when (position) {
            0 -> ScanQrFragment()     // Tab pertama untuk Scan QR
            1 -> ScanKodeFragment()  // Tab kedua untuk Kode
            else -> throw IllegalArgumentException("Posisi tidak valid: $position")
        }
    }
}
