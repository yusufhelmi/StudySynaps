package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class Assignment(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("deadline")
    val deadline: String
)
