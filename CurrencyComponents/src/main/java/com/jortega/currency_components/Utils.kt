package com.jortega.currencycomponents

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(
    value: Long,
    currencySymbol: String = "",
    notDecimals: Boolean = false,
    showZeros: Boolean = false,
    addSpaceAfterSymbol: Boolean = false
): String {
    val numberFormat = NumberFormat.getInstance(Locale.US)
    val formattedNumber = numberFormat.format(value / 100)

    val space = if (addSpaceAfterSymbol) " " else ""

    return if (notDecimals) {
        "$currencySymbol$space${numberFormat.format(value)}"
    } else if (showZeros) {
        "$currencySymbol$space$formattedNumber.00"
    } else {
        "$currencySymbol$space$formattedNumber.${String.format("%02d", value % 100)}"
    }
}