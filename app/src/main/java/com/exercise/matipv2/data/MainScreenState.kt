package com.exercise.matipv2.data

data class MainScreenState(
    var amountInput: String = "",
    var tipPercentInput: String = "",
    var roundUp: Boolean = false,
    var splitShare: Int = 0,
    var selectedTabIndex: Int = 0
)