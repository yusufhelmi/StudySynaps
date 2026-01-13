package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class ScheduleItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("course_name")
    val courseName: String,

    @SerializedName("lecturer_name")
    val lecturerName: String?,

    @SerializedName("day")
    val day: String,

    @SerializedName("start_time")
    val startTime: String, // Format "07:00:00"

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("room")
    val room: String,

    @SerializedName("type")
    val type: String // Teori / Praktikum
)
