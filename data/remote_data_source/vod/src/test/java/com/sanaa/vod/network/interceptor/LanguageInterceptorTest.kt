package com.sanaa.vod.network.interceptor

import android.content.Context
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test

class LanguageInterceptorTest {

    private val context = mockk<Context>()
    private val chain = mockk<Interceptor.Chain>()
    private val interceptor = LanguageInterceptor(context)


    @Test
    fun `adds language query parameter when Ignore-Language header not present`() {
        every { context.resources.configuration.locales[0].language } returns "en-US"
        val originalUrl = HttpUrl.Builder()
            .scheme("https")
            .host("example.com")
            .addPathSegment("path")
            .build()

        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val req = firstArg<Request>()
            Response.Builder()
                .request(req)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        val response = interceptor.intercept(chain)
        val proceededRequest = response.request


        Truth.assertThat(proceededRequest.url.queryParameter("language")).isEqualTo("en-US")
    }


    @Test
    fun `does not add language query parameter when Ignore-Language header present`() {
        val originalUrl = HttpUrl.Builder()
            .scheme("https")
            .host("example.com")
            .addPathSegment("path")
            .build()

        val originalRequest = Request.Builder()
            .url(originalUrl)
            .header("Ignore-Language", "true")
            .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(any()) } answers {
            val req = firstArg<Request>()
            Response.Builder()
                .request(req)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        val response = interceptor.intercept(chain)
        val proceededRequest = response.request

        assertThat(proceededRequest.url.queryParameter("language")).isNull()
    }
}