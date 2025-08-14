package com.sanaa.tvapp.di


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.tvapp.BuildConfig
import com.sanaa.vod.network.interceptor.APIKeyInterceptor
import com.sanaa.vod.network.interceptor.LanguageInterceptor
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideApiKeyInterceptor(
        preferencesManager: PreferencesManager
    ): APIKeyInterceptor = APIKeyInterceptor(
        apiKey = BuildConfig.TMDB_API_KEY,
        sessionId = { runBlocking { preferencesManager.sessionId.firstOrNull() } }
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: APIKeyInterceptor,
        languageInterceptor: LanguageInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(languageInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json; charset=UTF-8".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }
}
