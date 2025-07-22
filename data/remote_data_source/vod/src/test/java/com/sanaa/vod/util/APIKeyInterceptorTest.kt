package com.sanaa.vod.util

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.network.interceptor.APIKeyInterceptor
import io.mockk.every
import io.mockk.mockk
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test

class APIKeyInterceptorTest {

    private val apiKey = "my_api_key"
    private val interceptor = APIKeyInterceptor(apiKey)
    private val chain = mockk<Interceptor.Chain>()

    @Test
    fun `adds api_key query parameter to the request URL`() {
        val originalUrl = HttpUrl.Builder()
            .scheme("https")
            .host("example.com")
            .addPathSegment("test")
            .build()

        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val request = firstArg<Request>()
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        val response = interceptor.intercept(chain)
        val newRequest = response.request

        assertThat(newRequest.url.queryParameter("api_key")).isEqualTo(apiKey)
    }
}