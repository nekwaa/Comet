package com.example.comet

data class CometeerModel(
    val id: String,
    val name: String,
    val category: String,
    val rating: Float,
    val numReviews: Int,
    val distance: Double, // in km
    val imageUrl: String,
    val description: String,
    val hourlyRate: String
)