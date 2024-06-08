package com.wcsm.healthyfinance.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

class CurrencyVisualTransformation : VisualTransformation {
    private val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        // Removes all non-digit characters
        val digits = originalText.filter { it.isDigit() }

        // Converts the sequence of digits into a monetary value
        val parsed = digits.toLongOrNull() ?: 0L
        val formatted = formatter.format(parsed / 100.0)

        // Creates a new transformed text
        val out = AnnotatedString(formatted)

        // Character offset mapping
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return originalText.length
            }
        }

        return TransformedText(out, offsetMapping)
    }
}