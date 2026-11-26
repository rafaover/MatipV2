package com.exercise.matipv2.ui.tipcalculator

data class TipCalculatorScreenUiState(
    var tipAmount: String = "",
    var tipPercent: String = "",
    var roundUp: Boolean = false,
    var splitShare: Int = 1,
    var finalTip: String = "",
    var listId: Int = 0,
    var dateCreated: String = ""
)