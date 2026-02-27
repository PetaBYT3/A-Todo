package com.a.todo.extension

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getFutureDateByDaysAsLong(days: Int): Long {
    val today = LocalDate.now()
    val futureDate = today.plusDays(days.toLong())
    val zonedDateTime = futureDate.atStartOfDay(ZoneId.systemDefault())

    return zonedDateTime.toInstant().toEpochMilli()
}

fun getFutureDateByDaysAsString(days: Int): String {
    val today = LocalDate.now()
    val futureDate = today.plusDays(days.toLong())
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    return futureDate.format(formatter)
}

fun convertLongToString(timeMillis: Long): String {
    val instant = Instant.ofEpochMilli(timeMillis)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

    return localDate.format(formatter)
}