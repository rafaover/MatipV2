package com.exercise.matipv2.util

import android.icu.text.NumberFormat

fun convertTipAmountToCurrency(amount: String): String {
    val tipAmountToDouble = stringAmountToDouble(amount)
    return NumberFormat
        .getCurrencyInstance()
        .format(tipAmountToDouble)
}