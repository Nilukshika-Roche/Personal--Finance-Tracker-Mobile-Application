package com.example.mynilu.data

data class Transaction(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val date: String
)

