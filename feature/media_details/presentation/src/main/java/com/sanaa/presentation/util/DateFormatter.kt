package com.sanaa.presentation.util

import android.content.Context
import android.os.Build
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
fun getCurrentLocale(context: Context): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0]
    } else {
        context.resources.configuration.locale
    }
}

fun LocalDate.formatDateLocalizedDigits(locale: Locale = Locale.getDefault()): String {
    val day = dayOfMonth.toString().padStart(2, '0').toInt().toLocalizedDigits(locale)
    val month = monthNumber.toString().padStart(2, '0').toInt().toLocalizedDigits(locale)
    val year = year.toLocalizedDigits(locale)
    return "$day-$month-$year"
}