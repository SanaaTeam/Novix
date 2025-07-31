package com.sanaa.vod.network.interceptor

import com.sanaa.data.remotedatasource.vod.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(
    private val sessionId: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrlBuilder = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)

        sessionId()?.let {
            originalUrlBuilder.addQueryParameter("session_id", it)
        }

        val newUrl = originalUrlBuilder.build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
