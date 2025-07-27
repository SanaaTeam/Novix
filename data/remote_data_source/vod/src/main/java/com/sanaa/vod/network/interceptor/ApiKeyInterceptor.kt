package com.sanaa.vod.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(val token: () -> String) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${token()}")
            .build()
        return chain.proceed(newRequest)
    }
}