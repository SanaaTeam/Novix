package com.sanaa.vod.network.interceptor

import com.sanaa.data.remotedatasource.vod.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(val sessionId: () -> String?) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()

        sessionId()?.let { newRequest.addHeader("session_id", it) }
        newRequest.addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")

        return chain.proceed(newRequest.build())
    }
}