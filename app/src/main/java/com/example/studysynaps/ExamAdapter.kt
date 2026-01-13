package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.ExamItem
import java.text.SimpleDateFormat
import java.util.Locale

class ExamAdapter(private val examList: List<ExamItem>) :
    RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tv_day)
        val tvRoomSeat: TextView = view.findViewById(R.id.tv_room_seat)
        val tvOnlineBadge: TextView = view.findViewById(R.id.tv_online_badge)
        val tvLecturer: TextView = view.findViewById(R.id.tv_lecturer)
        val tvCourseInfo: TextView = view.findViewById(R.id.tv_course_info)
        val tvExamTime: TextView = view.findViewById(R.id.tv_exam_time)
        val tvExamDate: TextView = view.findViewById(R.id.tv_exam_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exam_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = examList[position]

        holder.tvDay.text = item.day ?: "-"
        holder.tvLecturer.text = item.lecturerName ?: "Dosen belum ditentukan"
        holder.tvCourseInfo.text = "${item.courseCode ?: ""} - ${item.courseName ?: ""}"

        // Format Date: 2025-11-19 -> 19-11-2025
        holder.tvExamDate.text = formatDate(item.examDate)

        // Format Time: 12:30:00 -> 12:30
        holder.tvExamTime.text = formatTime(item.startTime)

        if (item.type == "online") {
            holder.tvRoomSeat.visibility = View.GONE
            holder.tvOnlineBadge.visibility = View.VISIBLE
            
            // For online, if range exists
            if (item.onlineEndDate != null) {
                // Example: 17-11-2025 s.d 28-11-2025
                val start = formatDate(item.examDate)
                val end = formatDate(item.onlineEndDate)
                holder.tvExamTime.text = "$start s.d $end"
                holder.tvExamDate.visibility = View.GONE // Hide standard date
            }
        } else {
            holder.tvRoomSeat.visibility = View.VISIBLE
            holder.tvOnlineBadge.visibility = View.GONE
            holder.tvRoomSeat.text = "${item.room ?: "-"} / ${item.seatNumber ?: "-"}"
            holder.tvExamDate.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = examList.size

    private fun formatDate(dateStr: String?): String {
        if (dateStr.isNullOrEmpty()) return "-"
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = input.parse(dateStr)
            output.format(date!!)
        } catch (e: Exception) {
            dateStr
        }
    }

    private fun formatTime(timeStr: String?): String {
        if (timeStr.isNullOrEmpty()) return "-"
        return try {
            if (timeStr.length >= 5) timeStr.take(5) else timeStr
        } catch (e: Exception) {
            timeStr
        }
    }
}
