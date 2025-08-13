package com.sanaa.identity.util

import exceptions.InvalidUserOrPasswordException
import exceptions.LoginErrorException
import exceptions.NoNetworkException
import exceptions.NovixAppException
import retrofit2.HttpException
import java.net.UnknownHostException

inline fun <T> wrapApiCall(block: () -> T): T {
    return try {
        block()
    } catch (_: UnknownHostException) {
        throw NoNetworkException()
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        if (errorBody?.contains("status_code\":30") == true) {
            throw InvalidUserOrPasswordException()
        } else {
            throw LoginErrorException("HTTP ${e.code()}: ${errorBody ?: "Unknown server error"}")
        }
    } catch (_: Exception) {
        throw NovixAppException(message = "Unknown error")
    }
}