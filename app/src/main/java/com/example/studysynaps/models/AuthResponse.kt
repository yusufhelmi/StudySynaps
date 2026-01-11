package com.example.studysynaps.models

// Model ini akan menangkap JSON { "success": true, "message": "...", "data": {...} }
data class AuthResponse(
    val status: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData(
    val id: Int,
    val fullname: String,
    val email: String,
    val nim: String?,
    val prodi: String?
)
