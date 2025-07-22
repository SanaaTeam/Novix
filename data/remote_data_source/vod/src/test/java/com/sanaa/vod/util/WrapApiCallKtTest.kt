package com.sanaa.vod.util

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.util.exceptions.NoNetworkException
import com.sanaa.vod.util.exceptions.ParsingException
import com.sanaa.vod.util.exceptions.ServerErrorException
import com.sanaa.vod.util.exceptions.TimeoutException
import com.sanaa.vod.util.exceptions.UnknownDataSourceException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class WrapApiCallTest {

    @Test
    fun `should return result when execute succeeds`() = runTest {
        val result = runCatching {
            wrapApiCall { "Success" }
        }

        assertThat(result.getOrNull()).isEqualTo("Success")
        assertThat(result.exceptionOrNull()).isNull()
    }

    @Test
    fun `should throw NoNetworkException when UnknownHostException occurs`() = runTest {
        val exception = UnknownHostException()

        val result = runCatching {
            wrapApiCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(NoNetworkException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("No internet connection")
    }

    @Test
    fun `should throw TimeoutException when SocketTimeoutException occurs`() = runTest {
        val exception = SocketTimeoutException()

        val result = runCatching {
            wrapApiCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(TimeoutException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Request timed out")
    }

    @Test
    fun `should throw ServerErrorException when HttpException occurs`() = runTest {
        val responseBody = "".toResponseBody("application/json".toMediaType())
        val exception = HttpException(Response.error<Any>(500, responseBody))

        val result = runCatching {
            wrapApiCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(ServerErrorException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Server error")
    }

    @Test
    fun `should throw ParsingException when SerializationException occurs`() = runTest {
        val exception = SerializationException("bad json")

        val result = runCatching {
            wrapApiCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(ParsingException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Parsing error")
    }

    @Test
    fun `should throw UnknownDataSourceException when unknown Exception occurs`() = runTest {
        val exception = RuntimeException("unknown error")

        val result = runCatching {
            wrapApiCall { throw exception }
        }

        assertThat(result.exceptionOrNull()).isInstanceOf(UnknownDataSourceException::class.java)
        assertThat(result.exceptionOrNull()!!.message).isEqualTo("Unknown error")
    }
}