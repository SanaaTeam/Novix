package com.sanaa.search.di

import com.sanaa.search.network.KtorClient
import org.koin.dsl.module
import io.ktor.client.*

val networkModule = module {
    single<HttpClient> { KtorClient.client }
}
