package com.example.studysynaps

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.ScheduleItem

class AdapterJadwal(private var jadwalList: List<ScheduleItem>) :
    RecyclerView.Adapter<AdapterJadwal.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvJamMulai: TextView = view.findViewById(R.id.tv_jam_mulai)
        val tvJamSelesai: TextView = view.findViewById(R.id.tv_jam_selesai)
        val tvMatkul: TextView = view.findViewById(R.id.tv_matkul)
        val tvType: TextView = view.findViewById(R.id.tv_type)
        val tvRoom: TextView = view.findViewById(R.id.tv_room)
        val viewStrip: View = view.findViewById(R.id.view_color_strip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = jadwalList[position]

        // Format Time "07:00:00" -> "07:00"
        val start = item.startTime.take(5)
        val end = item.endTime.take(5)

        holder.tvJamMulai.text = start
        holder.tvJamSelesai.text = "- $end"
        holder.tvMatkul.text = item.courseName
        holder.tvType.text = item.type
        holder.tvRoom.text = item.room

        // Color Logic based on Type
        if (item.type == "Praktikum") {
            holder.viewStrip.setBackgroundColor(Color.parseColor("#F59E0B")) // Amber/Yellow
            holder.tvType.setTextColor(Color.parseColor("#B45309"))
            holder.tvType.setBackgroundResource(R.drawable.bg_badge_yellow_soft)
        } else {
            holder.viewStrip.setBackgroundColor(Color.parseColor("#2563EB")) // Blue
            holder.tvType.setTextColor(Color.parseColor("#1D4ED8"))
            holder.tvType.setBackgroundResource(R.drawable.bg_badge_blue_soft)
        }
    }

    override fun getItemCount() = jadwalList.size

    fun updateData(newList: List<ScheduleItem>) {
        jadwalList = newList
        notifyDataSetChanged()
    }
}
