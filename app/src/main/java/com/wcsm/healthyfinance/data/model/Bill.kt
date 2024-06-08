package com.wcsm.healthyfinance.data.model

import com.google.firebase.Timestamp
import com.wcsm.healthyfinance.ui.util.getDefaultTimestamp

data class Bill(
    val userId: String = "",
    val id: String = "",
    val description: String = "",
    val billCategory: BillCategory = BillCategory(),
    val value: Double = 0.0,
    val installment: Int = 0,
    val date: Timestamp = getDefaultTimestamp(),
)
