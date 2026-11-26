package com.exercise.matipv2.ui.tipcalculator

data class TipCalculatorScreenUiState(
    val tipAmount: String = "",
    val tipPercent: String = "",
    val roundUp: Boolean = false,
    val splitShare: Int = 1,
    val finalTip: String = "",
    val listId: Int = 0,
    val dateCreated: String = ""
)