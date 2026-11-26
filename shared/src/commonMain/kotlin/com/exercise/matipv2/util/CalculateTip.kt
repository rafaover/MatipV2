package com.exercise.matipv2.util

import kotlin.math.ceil

private const val PERCENT_BASE = 100.0

/**
 * The tip each person pays: [tipPercent] of [amount], shared by [tipSplit] people,
 * and rounded up to a whole number when [roundUp]. Text that is not a number counts as zero.
 */
fun calculateTipAmount(
    amount: String,
    tipPercent: String,
    tipSplit: Int = 1,
    roundUp: Boolean,
): Double {
    val tip = stringAmountToDouble(tipPercent) / PERCENT_BASE * stringAmountToDouble(amount)
    val share = if (tipSplit > 1) tip / tipSplit else tip
    return if (roundUp) ceil(share) else share
}

/** Same as [calculateTipAmount], formatted as money for the device's locale. */
fun calculateTip(
    amount: String,
    tipPercent: String,
    tipSplit: Int = 1,
    roundUp: Boolean,
): String = formatCurrency(calculateTipAmount(amount, tipPercent, tipSplit, roundUp))
