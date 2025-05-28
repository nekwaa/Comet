package com.example.comet

data class ReviewModel(
    val id: String,
    val transactionId: String,
    val cometeerName: String,
    val rating: Float,
    val comment: String?,
    val dateTime: String,
    var isEdited: Boolean = false
)