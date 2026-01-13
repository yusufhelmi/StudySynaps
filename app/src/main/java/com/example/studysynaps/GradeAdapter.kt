package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.GradeItem

class GradeAdapter(private val gradeList: List<GradeItem>) :
    RecyclerView.Adapter<GradeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_course_name)
        val tvCodeSks: TextView = view.findViewById(R.id.tv_course_code_sks)
        val tvGrade: TextView = view.findViewById(R.id.circle_grade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grade, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = gradeList[position]

        holder.tvName.text = item.courseName ?: "Unknown"
        holder.tvCodeSks.text = "${item.courseCode ?: "-"}  ${item.sks} SKS"
        holder.tvGrade.text = item.gradeLetter ?: "-"

        // Grade Color Logic
        val bgRes = when (item.gradeLetter) {
            "A" -> R.drawable.bg_circle_blue
            "AB", "B" -> R.drawable.bg_circle_green
            "BC", "C" -> R.drawable.bg_circle_yellow
            else -> R.drawable.bg_circle_yellow // Default/Red? Using Yellow for now as warning
        }
        holder.tvGrade.setBackgroundResource(bgRes)
    }

    override fun getItemCount() = gradeList.size
}
