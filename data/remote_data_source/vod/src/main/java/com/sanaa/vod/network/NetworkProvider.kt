package com.sanaa.vod.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sanaa.vod.network.interceptor.APIKeyInterceptor
import com.sanaa.vod.network.interceptor.LanguageInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


fun provideOkHttpClient(
    apiKey: String,
    languageInterceptor: LanguageInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(APIKeyInterceptor(apiKey))
        .addInterceptor(languageInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}

fun provideRetrofit(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    json: Json
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()
}