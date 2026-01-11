package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class Course(
    @SerializedName("id")
    val id: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("sks")
    val sks: Int,

    @SerializedName("is_taken")
    val isTaken: String = "0",

    // Helper for checkbox state (Local only)
    var isSelected: Boolean = false
)
