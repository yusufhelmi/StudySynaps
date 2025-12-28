package com.example.studysynaps

data class KrsItem(
    val id: Int,
    val namaMatkul: String,
    val namaDosen: String,
    val sks: Int,
    var isChecked: Boolean = false // Status untuk CheckBox
)
    