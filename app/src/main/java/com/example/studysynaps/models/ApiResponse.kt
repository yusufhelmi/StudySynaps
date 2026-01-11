package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
)
