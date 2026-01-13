package com.example.studysynaps

import com.google.android.material.chip.ChipGroup
import com.google.gson.annotations.SerializedName

data class PresensiItem(
    @SerializedName("course_name")
    val courseName: String,

    @SerializedName("code")
    val courseCode: String,
    
    @SerializedName("total_attendance")
    val attendanceScore: Int
)
