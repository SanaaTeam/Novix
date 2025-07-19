package com.sanaa.presentation.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatLocalizedDate(
    date: LocalDate,
    locale: Locale = Locale.getDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", locale)
    return date.toJavaLocalDate().format(formatter)
}


fun Int.toLocalizedDigits(locale: Locale): String {
    if (locale.language == "ar") {
        val arabicDigits = listOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return this.toString().map { arabicDigits[it - '0'] }.joinToString("")
    }
    return this.toString()
}

fun LocalDate.formatDateLocalizedDigits(locale: Locale = Locale.getDefault()): String {
    val day = dayOfMonth.toString().padStart(2, '0').toInt().toLocalizedDigits(locale)
    val month = monthNumber.toString().padStart(2, '0').toInt().toLocalizedDigits(locale)
    val year = year.toLocalizedDigits(locale)
    return "$day-$month-$year"
}