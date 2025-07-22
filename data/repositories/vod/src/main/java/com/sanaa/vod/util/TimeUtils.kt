package com.sanaa.vod.util

import kotlin.time.TimeSource

object TimeUtils {
    fun getCurrentTimeStamp(): Long {
        return TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds;
    }
}