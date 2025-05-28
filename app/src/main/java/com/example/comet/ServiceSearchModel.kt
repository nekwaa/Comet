package com.example.comet

data class ServiceSearchModel(
    val id: String,
    val name: String,
    val availableCount: Int,
    val category: String // To help with filtering or future enhancements
)