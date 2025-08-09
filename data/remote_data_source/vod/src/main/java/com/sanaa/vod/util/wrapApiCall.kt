package com.sanaa.vod.util

import android.util.Log
import com.sanaa.vod.util.exceptions.ConnectionException
import com.sanaa.vod.util.exceptions.ParsingException
import com.sanaa.vod.util.exceptions.ServerErrorException
import com.sanaa.vod.util.exceptions.TimeoutException
import com.sanaa.vod.util.exceptions.UnknownDataSourceException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> wrapApiCall(block: suspend () -> T): T {
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
    } catch (e: Exception) {
        Log.d("test99", "wrapApiCall: e:$e")
        throw UnknownDataSourceException(message = "Unknown error")
    }
}