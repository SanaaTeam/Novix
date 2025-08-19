package com.sanaa.tvapp.util

import android.content.Context
import android.os.Build
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

fun formatLocalizedDate(
    date: LocalDate,
    locale: Locale = Locale.getDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", locale)
    return date.toJavaLocalDate().format(formatter)
}