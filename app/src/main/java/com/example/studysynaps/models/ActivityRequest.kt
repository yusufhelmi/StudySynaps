package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class ActivityRequest(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("items")
    val items: List<ActivityItem>
)

data class ActivityItem(
    @SerializedName("material_id")
    val materialId: String,

    @SerializedName("notes")
    val notes: String = ""
)
