package com.wcsm.healthyfinance.data.model

import com.google.firebase.Timestamp
import com.wcsm.healthyfinance.ui.util.getDefaultTimestamp

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: Timestamp = getDefaultTimestamp(),
    val gender: String = "",
    val bills: List<Bill> = listOf()
)
