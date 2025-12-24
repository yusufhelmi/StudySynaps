package com.example.finalprojectbp2uts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KrsAdapter(private val krsList: List<KrsItem>) : RecyclerView.Adapter<KrsAdapter.KrsViewHolder>() {

    class KrsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaMatkul: TextView = view.findViewById(R.id.tv_nama_matkul)
        val tvNamaDosen: TextView = view.findViewById(R.id.tv_nama_dosen)
        val tvSks: TextView = view.findViewById(R.id.tv_sks)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox_krs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KrsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_krs, parent, false)
        return KrsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KrsViewHolder, position: Int) {
        val item = krsList[position]
        holder.tvNamaMatkul.text = item.namaMatkul
        holder.tvNamaDosen.text = item.namaDosen
        holder.tvSks.text = "${item.sks} SKS"

        // Set status CheckBox tanpa memicu listener
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = item.isChecked

        // Atur listener baru untuk menangani perubahan
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
            // Di sini Anda bisa menambahkan logika lain, misal menghitung total SKS
        }
    }

    override fun getItemCount() = krsList.size
}
    