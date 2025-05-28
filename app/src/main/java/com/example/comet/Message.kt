package com.example.comet

data class Message(
    val text: String,
    val time: String,
    val isSent: Boolean,
    var isBookingConfirmation: Boolean = false,
    var isRead: Boolean = false
)