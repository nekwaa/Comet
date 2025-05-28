package com.example.comet

data class CategoryModel(
    val id: String,
    val name: String,
    val iconResId: Int, // Resource ID for the category icon
    val description: String // Short description of the category
)