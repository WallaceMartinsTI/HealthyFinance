package com.wcsm.healthyfinance.data.model

import com.google.firebase.Timestamp
import com.wcsm.healthyfinance.ui.util.getDefaultTimestamp

data class AddBillFormState(
    val type: String = HistoryItemType.INCOME.toString(),
    val value: String = "",
    val description: String = "",
    val category: String = "",
    val date: Timestamp = getDefaultTimestamp(),
    val installment: Int = 0,
    val valueErrorMessage: String? = null,
    val descriptionErrorMessage: String? = null,
    val categoryErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isBillRegistered: Boolean = false,
)
