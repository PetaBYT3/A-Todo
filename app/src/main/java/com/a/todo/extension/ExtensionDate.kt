package com.a.todo.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getFutureDateByDaysAsString(days: Int): String {
    val today = LocalDate.now()
    val futureDate = today.plusDays(days.toLong())
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())

    return futureDate.format(formatter)
}

fun getFutureDateByDaysAsDate(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, days)

    return calendar.time
}