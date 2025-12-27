package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Nama kelas harus "JadwalAdapter"
class JadwalAdapter(private var jadwalList: List<Jadwal>) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    // Kelas ini memegang referensi ke setiap view di dalam satu kartu (item_jadwal_card.xml)
    class JadwalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDosen: TextView = view.findViewById(R.id.tv_dosen)
        val tvMatkul: TextView = view.findViewById(R.id.tv_matkul)
        val tvWaktu: TextView = view.findViewById(R.id.tv_waktu)
        val tvRuangan: TextView = view.findViewById(R.id.tv_ruangan)
        val tvTag: TextView = view.findViewById(R.id.tv_tag)
    }

    // Metode ini dipanggil untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jadwal_card, parent, false)
        return JadwalViewHolder(view)
    }

    // Metode ini dipanggil untuk mengisi data ke dalam kartu
    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        val jadwal = jadwalList[position] // Ambil data jadwal untuk posisi ini

        // Masukkan data ke dalam setiap TextView
        holder.tvDosen.text = jadwal.dosen
        holder.tvMatkul.text = jadwal.matkul
        holder.tvWaktu.text = jadwal.waktu
        holder.tvRuangan.text = jadwal.ruangan
        holder.tvTag.text = jadwal.tag
        // Di sini Anda bisa menambahkan logika untuk mengubah warna 'tvTag' berdasarkan 'jadwal.tagColor'
    }

    // Metode ini memberitahu RecyclerView berapa banyak total item yang ada di daftar
    override fun getItemCount(): Int {
        return jadwalList.size
    }

    // Fungsi untuk mengganti data di daftar dan memperbarui tampilan
    fun updateData(newJadwalList: List<Jadwal>) {
        this.jadwalList = newJadwalList
        notifyDataSetChanged() // Perintahkan RecyclerView untuk menggambar ulang dirinya
    }
}
