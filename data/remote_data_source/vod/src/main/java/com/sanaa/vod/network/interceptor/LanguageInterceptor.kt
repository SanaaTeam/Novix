package com.sanaa.vod.network.interceptor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(
    @ApplicationContext private val context: Context
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
            .addQueryParameter("language", getLanguageCode()).build()

        return chain.proceed(
            newRequest.newBuilder().url(newUrl).build()
        )
    }

    private fun getLanguageCode(): String {
        return context.resources.configuration.locales[0].language
    }
}