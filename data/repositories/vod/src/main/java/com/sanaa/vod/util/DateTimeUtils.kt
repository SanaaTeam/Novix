package com.sanaa.vod.util

import kotlinx.datetime.LocalDate

object DateTimeUtils {
    fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    fun getLocalDateOrDefault(date: String?): LocalDate {
        return try {
            LocalDate.parse(date ?: "")
        } catch (_: Exception) {
            defaultDate
        }
    }

    val defaultDate = LocalDate(1, 1, 1)
}