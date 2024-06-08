package com.wcsm.healthyfinance.ui.util

import com.wcsm.healthyfinance.data.model.HistoryItemType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Double.toBRL(): String {
    val ptBR = Locale("pt", "BR")
    return NumberFormat.getCurrencyInstance(ptBR).format(this)
}

fun Long.toBrazilianDateFormat(
    pattern: String = "dd/MM/yyyy"
): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(
        pattern, Locale("pt-br")
    ).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }
    return formatter.format(date)
}

fun String.toHistoryItemType(): HistoryItemType {
    return when(this) {
        "INCOME" -> HistoryItemType.INCOME
        "EXPENSE" -> HistoryItemType.EXPENSE
        "INVESTMENT" -> HistoryItemType.INVESTMENT
        else -> HistoryItemType.INCOME
    }
}