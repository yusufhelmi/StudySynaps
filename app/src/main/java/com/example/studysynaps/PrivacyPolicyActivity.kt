package com.example.studysynaps

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_privacy_policy)

        // Handle Edge-to-Edge for the toolbar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.app_bar_layout).setPadding(0, systemBars.top, 0, 0)
            insets
        }

        // Set up the back button
        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(0, 0) // Disable exit animation
        }

        // Find the TextView and set the privacy policy text
        val tvPrivacyPolicy: TextView = findViewById(R.id.tv_privacy_policy)
        tvPrivacyPolicy.text = getPrivacyPolicyText()
    }

    private fun getPrivacyPolicyText(): String {
        return """
        1. Pendahuluan
        Selamat datang di StudySynaps (â€œKamiâ€, â€œAplikasiâ€, atau â€œLayananâ€).
        Kebijakan Privasi ini menjelaskan bagaimana kami mengumpulkan, menggunakan, melindungi, dan membagikan informasi pribadi pengguna (â€œAndaâ€) ketika menggunakan layanan kami.
        Dengan menggunakan aplikasi ini, Anda dianggap telah membaca, memahami, dan menyetujui isi Kebijakan Privasi ini.

        2. Informasi yang Kami Kumpulkan
        Kami mengumpulkan beberapa jenis data untuk menunjang layanan akademik dan fitur pembelajaran daring di StudySynaps:
        a. Informasi Pribadi
        - Nama lengkap
        - Nomor Induk Mahasiswa (NIM) atau ID pengguna
        - Alamat email (terutama email institusi)
        - Nomor telepon (opsional, untuk verifikasi OTP)
        - Foto profil (jika diunggah)

        b. Data Akademik
        - Jadwal kuliah, presensi, tugas, nilai, dan aktivitas kelas
        - Unggahan tugas, komentar, dan interaksi di forum diskusi
        - Data kehadiran dan hasil evaluasi pembelajaran
        - Riwayat pembayaran atau transaksi akademik (jika tersedia)

        c. Data Teknis
        - Alamat IP, jenis perangkat, sistem operasi, dan versi aplikasi
        - Aktivitas pengguna seperti waktu login, lama penggunaan, dan interaksi dalam aplikasi

        3. Cara Kami Menggunakan Data
        Informasi yang dikumpulkan digunakan untuk:
        - Mengelola dan memverifikasi akun pengguna
        - Menyediakan akses ke fitur akademik dan kelas digital
        - Memfasilitasi komunikasi antara mahasiswa, dosen, dan pihak institusi
        - Meningkatkan keamanan serta performa aplikasi
        - Mengirimkan notifikasi akademik (seperti tugas, jadwal kuliah, dan pengumuman penting)
        - Menganalisis data penggunaan untuk mengembangkan fitur baru di StudySynaps

        4. Pembagian Data
        Kami tidak menjual atau menyewakan data pribadi pengguna kepada pihak mana pun.
        Namun, data dapat dibagikan kepada pihak lain dalam kondisi berikut:
        - Kepada institusi pendidikan terkait untuk kebutuhan administrasi akademik.
        - Kepada penyedia layanan pihak ketiga (seperti server cloud, verifikasi OTP, atau sistem keamanan) dengan perjanjian kerahasiaan yang ketat.
        - Jika diwajibkan oleh hukum atau diminta oleh otoritas yang berwenang.

        5. Keamanan Data
        Kami menerapkan langkah-langkah keamanan digital, termasuk:
        - Enkripsi data dan proteksi akses akun
        - Otentikasi dua langkah (OTP)
        - Pemantauan aktivitas mencurigakan untuk mencegah penyalahgunaan
        Meskipun demikian, kami tidak dapat menjamin keamanan mutlak terhadap semua potensi ancaman siber, tetapi kami akan berusaha maksimal untuk melindungi data Anda.

        6. Hak Pengguna
        Anda memiliki hak untuk:
        - Mengakses, memperbarui, atau menghapus data pribadi Anda.
        - Menarik persetujuan atas penggunaan data tertentu.
        - Meminta salinan data pribadi yang tersimpan di sistem kami.
        Permintaan dapat diajukan melalui email resmi support@studysynaps.com (atau alamat email dukungan resmi Anda).

        7. Penyimpanan dan Penghapusan Data
        Data pengguna disimpan selama akun masih aktif atau diperlukan untuk kegiatan akademik.
        Setelah akun dihapus, sebagian data akan dihapus secara permanen, sedangkan data tertentu dapat disimpan untuk keperluan arsip atau kepatuhan hukum.

        8. Cookies dan Pelacakan
        StudySynaps dapat menggunakan cookies atau teknologi serupa untuk menyimpan preferensi login, personalisasi antarmuka, dan meningkatkan pengalaman pengguna.
        Anda dapat menonaktifkan cookies di pengaturan perangkat, namun hal ini dapat mempengaruhi kinerja aplikasi.

        9. Perubahan Kebijakan Privasi
        Kami dapat memperbarui Kebijakan Privasi ini dari waktu ke waktu.
        Setiap perubahan akan diumumkan melalui aplikasi atau situs resmi StudySynaps, dan versi terbaru akan berlaku segera setelah diumumkan.

        10. Kontak Kami
        Jika Anda memiliki pertanyaan, saran, atau keluhan terkait Kebijakan Privasi ini, silakan hubungi kami melalui:
        ðŸ“§ support@studysynaps.com
        ðŸŒ www.studysynaps.com
        ðŸ“… Terakhir diperbarui: 28 Oktober 2025
        """.trimIndent()
    }
}
