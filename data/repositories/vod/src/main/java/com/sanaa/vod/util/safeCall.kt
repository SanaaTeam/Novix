package com.sanaa.vod.util

import com.sanaa.vod.util.exceptions.ConnectionException
import com.sanaa.vod.util.exceptions.ParsingException
import com.sanaa.vod.util.exceptions.ServerErrorException
import com.sanaa.vod.util.exceptions.TimeoutException
import com.sanaa.vod.util.exceptions.UnknownDataSourceException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException

inline fun <T> safeCall(
    errorMessage: String,
    exceptionProvider: (String) -> Exception = { msg -> RetrievingDataFailureException(msg) },
    block: () -> T
): T {
    try {
        return block()
    } catch (e: ConnectionException) {
        throw NoNetworkException()
    } catch (e: TimeoutException) {
        throw RetrievingDataFailureException(e.message ?: "Timeout occurred")
    } catch (e: ServerErrorException) {
        throw RetrievingDataFailureException(e.message ?: "Server error")
    } catch (e: ParsingException) {
        throw RetrievingDataFailureException(e.message ?: "Parsing error")
    } catch (e: UnknownDataSourceException) {
        throw RetrievingDataFailureException(e.message ?: "Unknown data source error")
    } catch (e: Exception) {
        throw exceptionProvider("$errorMessage: ${e.message}")
    }
}