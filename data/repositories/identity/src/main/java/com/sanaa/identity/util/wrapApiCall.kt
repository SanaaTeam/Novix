package com.sanaa.identity.util

import com.sanaa.identity.util.exceptions.ConnectionException
import com.sanaa.identity.util.exceptions.ParsingException
import com.sanaa.identity.util.exceptions.ResponseException
import com.sanaa.identity.util.exceptions.ServerErrorException
import com.sanaa.identity.util.exceptions.TimeoutException
import com.sanaa.identity.util.exceptions.UnknownDataSourceException
import exceptions.AuthenticationException
import exceptions.InvalidUserOrPasswordException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> wrapApiCall(block: () -> T): T {
    return try {
        block()
    } catch (e: UnknownHostException) {
        throw ConnectionException(message = "No internet connection")
    } catch (e: SocketTimeoutException) {
        throw TimeoutException(message = "Request timed out")
    } catch (e: HttpException) {
        throw ServerErrorException(message = "Server error")
    } catch (e: SerializationException) {
        throw ParsingException(message = "Parsing error")
    } catch (e: ResponseException) {
        if (e.errorCode == INVALID_USERNAME_OR_PASSWORD)
            throw InvalidUserOrPasswordException()
        else
            throw AuthenticationException(message = "Unknown error")
    } catch (e: Exception) {
        throw UnknownDataSourceException(message = "Unknown error")
    }
}


const val INVALID_USERNAME_OR_PASSWORD = 401