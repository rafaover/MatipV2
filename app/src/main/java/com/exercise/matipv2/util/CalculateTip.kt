package com.exercise.matipv2.util

import java.text.NumberFormat

fun calculateTip(
    amount: String,
    tipPercent: String,
    tipSplit: Int = 1,
    roundUp: Boolean,
): String {
    var tip = stringAmountToDouble(tipPercent) / 100 * stringAmountToDouble(amount)

    if (tipSplit > 1) tip /= tipSplit

    if(roundUp)
        tip = kotlin.math.ceil(tip)
    return NumberFormat.getCurrencyInstance().format(tip)
}