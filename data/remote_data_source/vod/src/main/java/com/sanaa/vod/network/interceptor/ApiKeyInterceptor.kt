package com.sanaa.vod.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(
    private val tokenProvider: () -> String,
    private val fallbackApiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider().ifBlank { fallbackApiKey }

        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val requestBuilder = originalRequest.newBuilder()

        if (token.startsWith("ey")) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        } else {
            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("api_key", token)
                .build()
            requestBuilder.url(newUrl)
        }

        return chain.proceed(requestBuilder.build())
    }
}