package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class ExamItem(
    val id: String? = null,
    @SerializedName("course_name")
    val courseName: String? = null,
    @SerializedName("course_code")
    val courseCode: String? = null,
    @SerializedName("lecturer_name")
    val lecturerName: String? = null,
    val day: String? = null,
    val room: String? = null,
    @SerializedName("seat_number")
    val seatNumber: String? = null,
    @SerializedName("exam_date")
    val examDate: String? = null,
    @SerializedName("start_time")
    val startTime: String? = null,
    @SerializedName("end_time")
    val endTime: String? = null,
    val type: String? = null,
    @SerializedName("online_end_date")
    val onlineEndDate: String? = null
)
