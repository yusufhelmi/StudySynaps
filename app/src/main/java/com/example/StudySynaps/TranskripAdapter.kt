package com.example.StudySynaps

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TranskripAdapter(private val transkripList: List<TranskripItem>) :
    RecyclerView.Adapter<TranskripAdapter.TranskripViewHolder>() {

    class TranskripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView = itemView.findViewById(R.id.tv_course_name)
        val tvCourseCode: TextView = itemView.findViewById(R.id.tv_course_code)
        val tvGradeBadge: TextView = itemView.findViewById(R.id.tv_grade_badge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranskripViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transkrip, parent, false)
        return TranskripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TranskripViewHolder, position: Int) {
        val item = transkripList[position]
        holder.tvCourseName.text = item.courseName
        holder.tvCourseCode.text = item.courseCode
        holder.tvGradeBadge.text = item.grade

        // Set background tint dynamically
        val background = holder.tvGradeBadge.background
        if (background is GradientDrawable) {
            background.setColor(item.gradeColor)
        } else {
             holder.tvGradeBadge.backgroundTintList = ColorStateList.valueOf(item.gradeColor)
        }
    }

    override fun getItemCount(): Int {
        return transkripList.size
    }
}
