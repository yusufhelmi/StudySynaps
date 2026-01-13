package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PresensiAdapter(private val presensiList: List<PresensiItem>) :
    RecyclerView.Adapter<PresensiAdapter.PresensiViewHolder>() {

    class PresensiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView = itemView.findViewById(R.id.tv_course_name)
        val tvCourseCode: TextView = itemView.findViewById(R.id.tv_course_code)
        val tvScoreBadge: TextView = itemView.findViewById(R.id.tv_score_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresensiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_presensi, parent, false)
        return PresensiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresensiViewHolder, position: Int) {
        val item = presensiList[position]
        holder.tvCourseName.text = item.courseName
        holder.tvCourseCode.text = item.courseCode
        holder.tvScoreBadge.text = item.attendanceScore.toString()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (item.courseId != null) {
                val intent = android.content.Intent(context, DetailPresensiActivity::class.java)
                intent.putExtra("EXTRA_COURSE_ID", item.courseId)
                intent.putExtra("EXTRA_COURSE_NAME", item.courseName ?: "Mata Kuliah")
                context.startActivity(intent)
            } else {
                android.widget.Toast.makeText(context, "Error: ID Mata Kuliah tidak ditemukan", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return presensiList.size
    }
}
