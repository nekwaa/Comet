package com.example.comet

data class TransactionModel(
    val id: String,
    val cometeerName: String,
    val cometeerImageUrl: String,
    val tags: List<String> = emptyList(),
    val serviceType: String,
    val dateTime: String,
    val cost: Double,
    val status: TransactionStatus
)

enum class TransactionStatus {
    ONGOING,
    COMPLETED
}