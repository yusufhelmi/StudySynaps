package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studysynaps.models.Material

class AdapterMateri(
    private var materials: List<Material>
) : RecyclerView.Adapter<AdapterMateri.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumbnail: ImageView = view.findViewById(R.id.img_materi) // Pastikan ID ini ada di layout
        val tvTitle: TextView = view.findViewById(R.id.tv_judul_materi)
        val tvCategory: TextView = view.findViewById(R.id.tv_deskripsi_materi) // Gunakan sbg kategori
        val btnAdd: AppCompatButton = view.findViewById(R.id.btn_kerjakan) // Gunakan tombol ini utk Add
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Menggunakan layout item_materi_card.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = materials[position]

        holder.tvTitle.text = item.title
        holder.tvCategory.text = item.category
        holder.btnAdd.text = "Buka Materi"

        // Load Image
        val imageUrl = "http://10.0.2.2/rest_api_synaps/uploads/${item.thumbnail}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background) 
            .into(holder.imgThumbnail)

        holder.btnAdd.setOnClickListener {
            val url = item.fileUrl
            if (url.isNotEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                holder.itemView.context.startActivity(intent)
            } else {
                 Toast.makeText(holder.itemView.context, "Link materi tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = materials.size

    fun updateData(newMaterials: List<Material>) {
        materials = newMaterials
        notifyDataSetChanged()
    }
}
