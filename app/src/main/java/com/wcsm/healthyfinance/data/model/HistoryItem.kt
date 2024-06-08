package com.wcsm.healthyfinance.data.model

data class HistoryItem(
    val description: String,
    val type: HistoryItemType,
    val value: Double,
    val date: String
)
