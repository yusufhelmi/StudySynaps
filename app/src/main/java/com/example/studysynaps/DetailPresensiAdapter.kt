package com.example.studysynaps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.AttendanceLog
import java.text.SimpleDateFormat
import java.util.*

class DetailPresensiAdapter(private val logs: List<AttendanceLog>) :
    RecyclerView.Adapter<DetailPresensiAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val viewIndicator: View = view.findViewById(R.id.view_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_presensi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = logs[position]
        
        holder.tvDate.text = "-"
        holder.tvTime.text = "-"

        if (item.createdAt != null) {
            // Parse "2026-01-13 16:10:55"
            try {
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = parser.parse(item.createdAt)
                
                if (date != null) {
                    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
                    holder.tvDate.text = dateFormatter.format(date)
                    
                    val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    holder.tvTime.text = timeFormatter.format(date)
                }
            } catch (e: Exception) {
                holder.tvDate.text = item.createdAt
            }
        }

        // Alternating Colors for Sidebar
        if (position % 2 == 0) {
            holder.viewIndicator.setBackgroundColor(android.graphics.Color.parseColor("#6200EE")) // Purple
        } else {
            holder.viewIndicator.setBackgroundColor(android.graphics.Color.parseColor("#FF9800")) // Orange
        }
    }

    override fun getItemCount() = logs.size
}
