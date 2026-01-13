package com.example.studysynaps.models

import com.google.gson.annotations.SerializedName

data class Billing(
    val id: String,
    
    @SerializedName("amount_due")
    val amountDue: String,
    
    @SerializedName("amount_paid")
    val amountPaid: String,
    
    val arrears: String,
    val deadline: String?,
    val status: String,
    
    @SerializedName("bank_name")
    val bankName: String?,
    
    @SerializedName("bank_account")
    val bankAccount: String?
)
