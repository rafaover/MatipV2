package com.exercise.matipv2.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn

fun localDateTimeFormated(): String {
    val todayDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val todayDateFormat = todayDate.format(LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    })

    return todayDateFormat
}