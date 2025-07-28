package com.sanaa.novix.di


import com.sanaa.novix.BuildConfig
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.network.interceptor.LanguageInterceptor
import com.sanaa.vod.network.provideOkHttpClient
import com.sanaa.vod.network.provideRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKotlinxJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Provides
    @Singleton
    fun provideKtorClient(json: Json): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json(json) }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        engine { requestTimeout = 30_000 }
    }

    @Provides
    @Singleton
    fun provideLanguageInterceptor(
        languageProvider: LanguageProvider
    ): LanguageInterceptor = LanguageInterceptor(languageProvider)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        languageInterceptor: LanguageInterceptor
    ): OkHttpClient =
        provideOkHttpClient(
            apiKey = BuildConfig.TMDB_API_KEY,
            languageInterceptor = languageInterceptor
        )

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit =
        provideRetrofit(
            baseUrl = BuildConfig.TMDB_URL,
            okHttpClient = okHttpClient,
            json = json
        )
}
