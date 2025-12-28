package com.example.studysynaps

// Nama kelas harus "Jadwal"
data class Jadwal(
    val dosen: String,
    val matkul: String,
    val waktu: String,
    val ruangan: String,
    val tag: String,
    val tagColor: Int // <- Tambahkan parameter ini untuk menampung warna
)
