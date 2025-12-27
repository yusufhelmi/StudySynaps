package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class MateriTugasItem(
    val title: String,
    val subtitle: String, // Deadline for tugas, or generic for materi
    val dateFooter: String,
    val type: ItemType
)

enum class ItemType {
    MATERI, TUGAS
}

class MateriTugasAdapter(private var items: List<MateriTugasItem>) :
    RecyclerView.Adapter<MateriTugasAdapter.ViewHolder>() {

    fun updateData(newItems: List<MateriTugasItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.iv_icon)
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvSubtitle: TextView = view.findViewById(R.id.tv_subtitle)
        val tvDateFooter: TextView = view.findViewById(R.id.tv_date_footer)
        val btnOptions: ImageView = view.findViewById(R.id.btn_options)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi_tugas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle
        holder.tvDateFooter.text = item.dateFooter

        if (item.type == ItemType.MATERI) {
            // Materi Style
            holder.ivIcon.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder for PDF
            holder.ivIcon.setColorFilter(android.graphics.Color.RED)
            
            holder.tvSubtitle.visibility = View.GONE
            holder.tvDateFooter.visibility = View.VISIBLE
            holder.btnOptions.visibility = View.GONE
        } else {
            // Tugas Style
            holder.ivIcon.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder for Task
            holder.ivIcon.setColorFilter(android.graphics.Color.BLUE)

            holder.tvSubtitle.visibility = View.VISIBLE
            holder.tvSubtitle.setTextColor(android.graphics.Color.BLUE) // Or specific color
            
            holder.tvDateFooter.visibility = View.VISIBLE
            holder.btnOptions.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            if (item.type == ItemType.TUGAS) {
                 val context = holder.itemView.context
                 val intent = android.content.Intent(context, DetailTugasActivity::class.java)
                 intent.putExtra("EXTRA_TITLE", item.title)
                 context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = items.size
}
