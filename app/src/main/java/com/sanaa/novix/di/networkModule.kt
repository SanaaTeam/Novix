package com.sanaa.novix.di

import com.sanaa.novix.BuildConfig
import com.sanaa.vod.network.interceptor.LanguageInterceptor
import com.sanaa.vod.network.provideOkHttpClient
import com.sanaa.vod.network.provideRetrofit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
        provideOkHttpClient(
            apiKey = BuildConfig.TMDB_API_KEY,
            languageInterceptor = LanguageInterceptor(get())
        )
    }
    single<Retrofit> {
        provideRetrofit(
            baseUrl = BuildConfig.TMDB_URL,
            okHttpClient = get(),
            json = get()
        )
    }
}