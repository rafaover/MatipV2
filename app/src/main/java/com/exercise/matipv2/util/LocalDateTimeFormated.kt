package com.exercise.matipv2.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun localDateTimeFormated(): String {
    val todayDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val todayDateFormat = todayDate.format(LocalDate.Format {
        day()
        char('/')
        monthNumber()
        char('/')
        year()
    })

    return todayDateFormat
}