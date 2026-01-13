package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class GradeResponse(
    val semester: String?,
    @SerializedName("total_sks")
    val totalSks: Int,
    val ips: Double,
    val courses: List<GradeItem>
)

data class GradeItem(
    val id: String?,
    @SerializedName("course_name")
    val courseName: String?,
    @SerializedName("course_code")
    val courseCode: String?,
    val sks: Int,
    @SerializedName("grade_letter")
    val gradeLetter: String?,
    @SerializedName("grade_point")
    val gradePoint: Double
)
