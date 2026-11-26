package com.exercise.matipv2.util

import java.text.NumberFormat

/** Formats [amount] with the device locale's currency, e.g. "$1.50". */
fun formatCurrency(amount: Double): String = NumberFormat.getCurrencyInstance().format(amount)
