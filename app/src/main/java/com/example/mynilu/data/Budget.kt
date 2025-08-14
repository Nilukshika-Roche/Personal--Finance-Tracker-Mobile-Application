package com.example.mynilu.data




data class Budget(
    val id: Long,           // Unique ID (use System.currentTimeMillis())
    var amount: Float,
    var date: String        // example: "April 2025"
)
