package com.sanaa.vod.util

import android.util.Log
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import timber.log.Timber

inline fun <T> safeCall(
    errorMessage: String,
    block: () -> T
): T {
    try {
        return block()
    } catch (e: ConnectionException) {
        Timber.w(e, "Connection issue: No network")
        throw NoNetworkException()
    } catch (e: Exception) {
        Timber.w(e, "Unexpected error in safeCall")
        throw NovixAppException("$errorMessage: ${e.message}")
    }
}