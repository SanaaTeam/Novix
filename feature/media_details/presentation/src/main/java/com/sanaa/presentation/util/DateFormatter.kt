package com.sanaa.presentation.util

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

fun getCurrentLocale(context: Context): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0]
    } else {
        context.resources.configuration.locale
    }
}
fun formatTime(duration: Duration, locale: Locale = Locale.getDefault()): String {

    val hours = duration.inWholeHours.toInt()
    val minutes = (duration - duration.inWholeHours.hours).inWholeMinutes.toInt()

    if (locale.language.lowercase() == "ar") {
        fun hourLabel(hour: Int): String = when (hour) {
            0 -> ""
            1 -> "ساعة واحدة"
            2 -> "ساعتان"
            in 3..10 -> "$hour ساعات"
            else -> "$hour ساعة"
        }

        fun minuteLabel(minute: Int): String = when (minute) {
            0 -> ""
            1 -> "دقيقة واحدة"
            2 -> "دقيقتان"
            in 3..10 -> "$minute دقائق"
            else -> "$minute دقيقة"
        }

        return "${hourLabel(hours)} و ${minuteLabel(minutes)}"
    } else {
        fun hourLabel(hour: Int): String = when (hour) {
            1 -> "${hour}h"
            else -> "${hour}h"
        }

        fun minuteLabel(minute: Int): String = when (minute) {
            1 -> "${minute}m"
            else -> "${minute}m"
        }

        return "${hourLabel(hours)} ${minuteLabel(minutes)}"
    }
}
