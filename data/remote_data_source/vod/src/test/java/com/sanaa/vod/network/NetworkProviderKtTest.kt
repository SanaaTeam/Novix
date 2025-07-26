package com.sanaa.vod.network


import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.network.interceptor.APIKeyInterceptor
import com.sanaa.vod.network.interceptor.LanguageInterceptor
import io.mockk.mockk
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test
import retrofit2.Retrofit

class NetworkProviderTest {
    @Test
    fun `provideOkHttpClient returns OkHttpClient with expected interceptors`() {
        val client = provideOkHttpClient("fake-api-key", dummyInterceptor)

        val interceptors: List<Interceptor> = client.interceptors

        assertThat(interceptors.any { it is APIKeyInterceptor }).isTrue()
        assertThat(interceptors.any { it === dummyInterceptor }).isTrue()
        assertThat(interceptors.any { it is okhttp3.logging.HttpLoggingInterceptor }).isTrue()
    }

    @Test
    fun `provideRetrofit builds Retrofit instance correctly`() {
        val json = Json { ignoreUnknownKeys = true }
        val client = OkHttpClient.Builder().build()
        val baseUrl = "https://api.example.com/"

        val retrofit: Retrofit = provideRetrofit(baseUrl, client)

        assertThat(retrofit.baseUrl().toString()).isEqualTo(baseUrl)
        assertThat(retrofit.callFactory()).isSameInstanceAs(client)
    }

    companion object {
        val dummyInterceptor = LanguageInterceptor(
            mockk()
        )
    }
}