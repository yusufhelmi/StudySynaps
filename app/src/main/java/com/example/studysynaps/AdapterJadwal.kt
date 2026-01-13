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
        val tvLecturer: TextView = view.findViewById(R.id.tv_lecturer) // Added
        val tvType: TextView = view.findViewById(R.id.tv_type)
        val tvRoom: TextView = view.findViewById(R.id.tv_room)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = jadwalList[position]

        // Format Time "07:00:00" -> "07.00"
        val start = item.startTime.take(5).replace(":", ".")
        val end = item.endTime.take(5).replace(":", ".")

        holder.tvJamMulai.text = start
        holder.tvJamSelesai.text = " - $end"
        holder.tvMatkul.text = item.courseName
        holder.tvType.text = item.type
        holder.tvRoom.text = item.room
        
        // Bind Lecturer
        val lecturer = item.lecturerName ?: "Dosen Belum Ditentukan"
        holder.tvLecturer.text = lecturer

        // Color Logic based on Type (Background Solid)
        if (item.type == "Praktikum") {
            holder.tvType.setBackgroundResource(R.drawable.bg_badge_orange) // Orange Solid
            holder.tvType.setTextColor(Color.WHITE)
        } else {
            holder.tvType.setBackgroundResource(R.drawable.bg_badge_blue_solid) // Blue Solid
            holder.tvType.setTextColor(Color.WHITE)
        }
    }

    override fun getItemCount() = jadwalList.size

    fun updateData(newList: List<ScheduleItem>) {
        jadwalList = newList
        notifyDataSetChanged()
    }
}
