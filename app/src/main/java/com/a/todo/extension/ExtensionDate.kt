package com.a.todo.extension

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

fun getDelayUntilMidnight(): Long {
    val zone = ZoneId.systemDefault()
    val now = LocalDateTime.now(zone)
    val midnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)

    return Duration.between(now, midnight).toMinutes()
}

fun getDelayUntilMorning(): Long {
    val zone = ZoneId.systemDefault()
    val now = ZonedDateTime.now(zone)
    var target = now.withHour(6).withMinute(0).withSecond(0).withNano(0)

    if (now.isAfter(target)) {
        target = target.plusDays(1)
    }

    return Duration.between(now, target).toMinutes()
}