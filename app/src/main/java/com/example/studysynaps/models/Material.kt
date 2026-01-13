package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class Material(
    @SerializedName("material_id")
    val materialId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("thumbnail")
    val thumbnail: String,

    @SerializedName("file_url")
    val fileUrl: String
)
