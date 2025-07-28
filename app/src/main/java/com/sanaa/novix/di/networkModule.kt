package com.sanaa.novix.di

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.novix.BuildConfig
import com.sanaa.vod.network.interceptor.LanguageInterceptor
import com.sanaa.vod.network.interceptor.provideOkHttpClient
import com.sanaa.vod.network.interceptor.provideRetrofit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module

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
            tokenProvider = {
                runBlocking {
                    get<PreferencesManager>().authorizationToken.firstOrNull().orEmpty()
                }
            },
            languageInterceptor = LanguageInterceptor(get())
        )
    }

    single {
        provideRetrofit(
            baseUrl = BuildConfig.TMDB_URL,
            okHttpClient = get(),
            json = get()
        )
    }

}