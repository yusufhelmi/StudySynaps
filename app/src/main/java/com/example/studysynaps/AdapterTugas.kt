package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.Assignment

class AdapterTugas(
    private var assignments: List<Assignment>
) : RecyclerView.Adapter<AdapterTugas.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvSubtitle: TextView = view.findViewById(R.id.tv_subtitle) // Deadline
        val tvDateFooter: TextView = view.findViewById(R.id.tv_date_footer) // Created At or similar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Reuse existing layout or create new one. using item_materi_tugas for now as it fits.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi_tugas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = assignments[position]

        holder.tvTitle.text = item.title
        if (item.isSubmitted) {
            holder.tvSubtitle.text = "Selesai"
            holder.tvSubtitle.setTextColor(android.graphics.Color.parseColor("#4CAF50")) // Green
            holder.tvDateFooter.visibility = View.GONE
        } else {
            holder.tvSubtitle.text = "Deadline: ${item.deadline}"
            holder.tvSubtitle.setTextColor(android.graphics.Color.parseColor("#DC2626")) // Red
            holder.tvDateFooter.visibility = View.VISIBLE
        }
        
        // Klik item untuk membuka detail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = android.content.Intent(context, DetailTugasActivity::class.java)
            intent.putExtra("EXTRA_ID", item.id) 
            intent.putExtra("EXTRA_TITLE", item.title)
            intent.putExtra("EXTRA_DESC", item.description)
            intent.putExtra("EXTRA_DEADLINE", item.deadline)
            intent.putExtra("EXTRA_IS_SUBMITTED", item.isSubmitted)
            context.startActivity(intent)
        } 
    }

    override fun getItemCount() = assignments.size

    fun updateData(newAssignments: List<Assignment>) {
        assignments = newAssignments
        notifyDataSetChanged()
    }
}
