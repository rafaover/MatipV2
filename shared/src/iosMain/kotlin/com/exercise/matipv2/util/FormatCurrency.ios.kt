package com.exercise.matipv2.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual fun formatCurrency(amount: Double): String {
    val formatter = NSNumberFormatter().apply { numberStyle = NSNumberFormatterCurrencyStyle }
    return formatter.stringFromNumber(NSNumber(double = amount)) ?: amount.toString()
}
