package com.wcsm.healthyfinance.ui.util

import android.content.Context
import android.widget.Toast
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// To use in default values in Models for Firestore
fun getDefaultTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, 1950)
    calendar.set(Calendar.MONTH, 0)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    return Timestamp(calendar.time)
}

fun parseDateFromString(dateString: String): Timestamp? {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val date = format.parse(dateString)
    return if(date != null) Timestamp(date) else null
}

fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun formatTimestamp(timestamp: Timestamp?): String {
    return if(timestamp != null) {
        val date = timestamp.toDate()
        val format = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        return format.format(date)
    } else {
        "Não encontrada."
    }
}

class ValueAndPercentageFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return String.format("%.1f%%", value)
    }
}