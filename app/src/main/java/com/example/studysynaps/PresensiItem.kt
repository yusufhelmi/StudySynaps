package com.example.studysynaps

import com.google.android.material.chip.ChipGroup
import com.google.gson.annotations.SerializedName

data class PresensiItem(
    @SerializedName("course_id")
    val courseId: String? = null,

    @SerializedName("course_name")
    val courseName: String? = null,

    @SerializedName("code")
    val courseCode: String? = null,
    
    @SerializedName("total_attendance")
    val attendanceScore: Int = 0
)
