package com.sanaa.tvapp.util

import android.content.Context
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

object DateTimeUtils {

    fun formatLocalizedDate(
        date: LocalDate,
        locale: Locale = Locale.getDefault(),
    ): String {
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", locale)
        return date.toJavaLocalDate().format(formatter)
    }

    fun getCurrentLocale(context: Context): Locale {
        return context.resources.configuration.locales[0]
    }

    fun formatTime(duration: Duration, locale: Locale = Locale.getDefault()): String {

        val hours = duration.inWholeHours.toInt()
        val minutes = (duration - duration.inWholeHours.hours).inWholeMinutes.toInt()

        if (locale.language.lowercase() == "ar") {
            fun hourLabel(hour: Int): String = when (hour) {
                0 -> ""
                1 -> "ساعة"
                2 -> "ساعتان"
                in 3..10 -> "$hour ساعات"
                else -> "$hour ساعة"
            }

            fun minuteLabel(minute: Int): String = when (minute) {
                0 -> ""
                1 -> "دقيقة"
                2 -> "دقيقتان"
                in 3..10 -> "$minute دقائق"
                else -> "$minute دقيقة"
            }

            val hoursText = hourLabel(hours)
            val minutesText = minuteLabel(minutes)

            return when {
                hours > 0 && minutes > 0 -> "$hoursText و $minutesText"
                hours > 0 -> hoursText
                minutes > 0 -> minutesText
                else -> "0 دقيقة"
            }
        } else {
            fun hourLabel(hour: Int): String = "${hour}h"
            fun minuteLabel(minute: Int): String = "${minute}m"

            val parts = mutableListOf<String>()
            if (hours > 0) parts.add(hourLabel(hours))
            if (minutes > 0) parts.add(minuteLabel(minutes))

            return if (parts.isNotEmpty()) parts.joinToString(" ") else "0m"
        }
    }

    val defaultDate = LocalDate(1, 1, 1)
}