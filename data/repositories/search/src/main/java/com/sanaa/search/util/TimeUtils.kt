package com.sanaa.search.util

import kotlin.time.TimeSource

object TimeUtils {
    fun getCurrentTimeStamp(): Long {
        return TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds;
    }
}