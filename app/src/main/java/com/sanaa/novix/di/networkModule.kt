package com.sanaa.novix.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sanaa.novix.BuildConfig
import com.sanaa.vod.interceptor.APIKeyInterceptor
import com.sanaa.vod.interceptor.LanguageInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(get())
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            engine {
                requestTimeout = 30_000
            }
        }
    }


    single {
        OkHttpClient.Builder().addInterceptor(APIKeyInterceptor(BuildConfig.TMDB_API_KEY))
            .addInterceptor(LanguageInterceptor(get()))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }


    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.TMDB_URL).client(get()).build()
    }
}