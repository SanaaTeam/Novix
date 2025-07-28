package com.sanaa.vod.util

import com.sanaa.vod.util.exceptions.ConnectionException
import com.sanaa.vod.util.exceptions.ParsingException
import com.sanaa.vod.util.exceptions.ServerErrorException
import com.sanaa.vod.util.exceptions.TimeoutException
import com.sanaa.vod.util.exceptions.UnknownDataSourceException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import timber.log.Timber

inline fun <T> safeCall(
    errorMessage: String,
    exceptionProvider: (String) -> Exception = { msg -> RetrievingDataFailureException(msg) },
    block: () -> T
): T {
    try {
        return block()
    } catch (e: ConnectionException) {
        Timber.w(e, "Connection issue: No network")
        throw NoNetworkException()
    } catch (e: TimeoutException) {
        Timber.w(e, "Timeout occurred")
        throw RetrievingDataFailureException(e.message ?: "Timeout occurred")
    } catch (e: ServerErrorException) {
        Timber.e(e, "Server error")
        throw RetrievingDataFailureException(e.message ?: "Server error")
    } catch (e: ParsingException) {
        Timber.e(e, "Parsing error")
        throw RetrievingDataFailureException(e.message ?: "Parsing error")
    } catch (e: UnknownDataSourceException) {
        Timber.e(e, "Unknown data source error")
        throw RetrievingDataFailureException(e.message ?: "Unknown data source error")
    } catch (e: Exception) {
        Timber.e(e, "Unexpected error in safeCall")
        throw exceptionProvider("$errorMessage: ${e.message}")
    }
}