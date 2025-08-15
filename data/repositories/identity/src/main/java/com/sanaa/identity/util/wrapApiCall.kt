package com.sanaa.identity.util

import exceptions.IdentityException
import exceptions.InvalidUserOrPasswordException
import exceptions.LoginErrorException
import exceptions.NoInternetException
import retrofit2.HttpException
import java.net.UnknownHostException

inline fun <T> wrapApiCall(block: () -> T): T {
    return try {
        block()
    } catch (_: UnknownHostException) {
        throw NoInternetException()
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        if (errorBody?.contains("status_code\":30") == true) {
            throw InvalidUserOrPasswordException()
        } else {
            throw LoginErrorException("HTTP ${e.code()}: ${errorBody ?: "Unknown server error"}")
        }
    } catch (_: Exception) {
        throw IdentityException(message = "Unknown error")
    }
}