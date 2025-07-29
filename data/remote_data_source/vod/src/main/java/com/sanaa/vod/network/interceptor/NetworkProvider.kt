package com.sanaa.vod.network.interceptor


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sanaa.data.remotedatasource.vod.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

fun provideOkHttpClient(
    tokenProvider: () -> String,
    languageInterceptor: LanguageInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(APIKeyInterceptor(tokenProvider, BuildConfig.TMDB_API_KEY))
        .addInterceptor(languageInterceptor)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }
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