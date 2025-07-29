package com.sanaa.vod.network.interceptor

import com.sanaa.data.remotedatasource.vod.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(val sessionId: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .apply {
                sessionId()?.let { addHeader("session_id", it) }
            }
            .build()

        return chain.proceed(newRequest)
    }
}