package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.Course

class KrsAdapter(
    private var courses: List<Course>,
    private val onSksChanged: (Int) -> Unit
) : RecyclerView.Adapter<KrsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbCourse: CheckBox = view.findViewById(R.id.cb_course)
        val tvName: TextView = view.findViewById(R.id.tv_course_name)
        val tvCode: TextView = view.findViewById(R.id.tv_course_code)
        val tvSks: TextView = view.findViewById(R.id.tv_sks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]

        holder.tvName.text = course.name
        holder.tvCode.text = course.code
        holder.tvSks.text = "${course.sks} SKS"
        val alreadyTaken = course.isTaken == "1"

        // Update logical state as well
        if (alreadyTaken) {
            course.isSelected = true
        }

        if (alreadyTaken) {
            holder.cbCourse.isChecked = true
            holder.cbCourse.isEnabled = false // Disable user interaction
            holder.tvName.setTextColor(android.graphics.Color.GRAY) // Gray out text
            holder.tvCode.setTextColor(android.graphics.Color.GRAY) // Gray out code
            holder.tvSks.setTextColor(android.graphics.Color.GRAY)
            holder.itemView.setOnClickListener(null) // Disable item click
            
            // Remove listener to prevent double triggering calculate logic during bind
            holder.cbCourse.setOnCheckedChangeListener(null)
        } else {
            // Normal state
            holder.cbCourse.isEnabled = true
            holder.tvName.setTextColor(android.graphics.Color.BLACK)
            holder.tvCode.setTextColor(android.graphics.Color.parseColor("#4B5563"))
            holder.tvSks.setTextColor(android.graphics.Color.BLACK)

            holder.cbCourse.setOnCheckedChangeListener(null) // Clear previous
            holder.cbCourse.isChecked = course.isSelected
            
            holder.cbCourse.setOnCheckedChangeListener { _, isChecked ->
                course.isSelected = isChecked
                calculateTotalSks()
            }

            holder.itemView.setOnClickListener {
                holder.cbCourse.isChecked = !holder.cbCourse.isChecked
            }
        }
    }

    override fun getItemCount() = courses.size

    fun updateData(newCourses: List<Course>) {
        // Pre-process: Set isSelected=true if course is already taken
        newCourses.forEach { 
            if (it.isTaken == "1") {
                it.isSelected = true
            }
        }
        
        courses = newCourses
        notifyDataSetChanged()
        calculateTotalSks() // Now this will see the correct isSelected values
    }

    private fun calculateTotalSks() {
        val total = courses.filter { it.isSelected }.sumOf { it.sks }
        onSksChanged(total)
    }
    
    fun getSelectedCourseIds(): List<String> {
        return courses.filter { it.isSelected }.map { it.id }
    }

    fun hasNewSelections(): Boolean {
        // Return true if there is at least one course selected that wasn't previously taken
        return courses.any { it.isSelected && it.isTaken == "0" }
    }
}