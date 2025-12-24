package com.example.finalprojectbp2uts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class HasilStudiItem(
    val title: String,
    val sks: String,
    val grade: String
)

class HasilStudiAdapter(private val items: List<HasilStudiItem>) :
    RecyclerView.Adapter<HasilStudiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_course_name)
        val tvSks: TextView = view.findViewById(R.id.tv_sks)
        val tvGrade: TextView = view.findViewById(R.id.tv_grade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hasil_studi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvSks.text = item.sks
        holder.tvGrade.text = item.grade
    }

    override fun getItemCount() = items.size
}
