package com.sanaa.tvapp.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatLocalizedDate(
    date: LocalDate,
    locale: Locale = Locale.getDefault(),
): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", locale)
    return date.toJavaLocalDate().format(formatter)
}