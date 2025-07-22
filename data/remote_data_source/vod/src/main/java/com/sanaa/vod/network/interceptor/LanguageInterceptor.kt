package com.sanaa.vod.network.interceptor

import com.sanaa.preferences.service.LanguageProvider
import okhttp3.Interceptor
import okhttp3.Response

open class LanguageInterceptor(
    private val languageProvider: LanguageProvider
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
            .addQueryParameter("language", languageProvider.getCurrentLanguage()).build()

        return chain.proceed(
            newRequest.newBuilder().url(newUrl).build()
        )
    }
}