package com.exercise.matipv2.util

fun stringAmountToDouble(amount: String): Double {
    return amount.toDoubleOrNull() ?: 0.0
}