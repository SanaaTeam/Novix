package com.sanaa.vod.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(
    private val getLanguage: () -> String?
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val ignoreLanguage = original.header("Ignore-Language") != null
        val newRequest = original.newBuilder().removeHeader("Ignore-Language").build()

        if (ignoreLanguage) {
            return chain.proceed(newRequest)
        }

        val originalUrl = original.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("language", getLanguage()).build()

        return chain.proceed(
            newRequest.newBuilder().url(newUrl).build()
        )
    }
}