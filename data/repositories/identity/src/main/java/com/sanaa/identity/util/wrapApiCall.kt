package com.sanaa.identity.util

import com.sanaa.identity.util.exceptions.ConnectionException
import com.sanaa.identity.util.exceptions.ParsingException
import com.sanaa.identity.util.exceptions.ServerErrorException
import com.sanaa.identity.util.exceptions.TimeoutException
import com.sanaa.identity.util.exceptions.UnknownDataSourceException
import exceptions.InvalidUserOrPasswordException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> wrapApiCall(block: () -> T): T {
    return try {
        block()
    } catch (_: UnknownHostException) {
        throw ConnectionException(message = "No internet connection")
    } catch (_: SocketTimeoutException) {
        throw TimeoutException(message = "Request timed out")
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        if (errorBody?.contains("status_code\":30") == true) {
            throw InvalidUserOrPasswordException()
        } else {
            throw ServerErrorException("HTTP ${e.code()}: ${errorBody ?: "Unknown server error"}")
        }
    } catch (_: SerializationException) {
        throw ParsingException(message = "Parsing error")
    } catch (_: Exception) {
        throw UnknownDataSourceException(message = "Unknown error")
    }
}