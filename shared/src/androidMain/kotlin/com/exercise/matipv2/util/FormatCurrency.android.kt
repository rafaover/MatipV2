package com.exercise.matipv2.util

import java.text.NumberFormat

actual fun formatCurrency(amount: Double): String = NumberFormat.getCurrencyInstance().format(amount)
