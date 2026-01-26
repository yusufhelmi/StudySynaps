package com.example.studysynaps.models

// Model ini akan menangkap JSON { "success": true, "message": "...", "data": {...} }
data class AuthResponse(
    val status: Boolean,
    val message: String,
    @com.google.gson.annotations.SerializedName("user_id")
    val userId: Int?, // For Forgot Password Response
    val data: UserData?
)

data class UserData(
    val id: Int,
    val fullname: String,
    val email: String,
    val nim: String?,
    val prodi: String?,
    val status: String? = "active", // Default to active to avoid crash if null
    @com.google.gson.annotations.SerializedName("payment_status")
    val paymentStatus: String? = "unpaid",
    val photo: String?
)
