package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class AttendanceLog(
    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("status")
    val status: String? = null
)
