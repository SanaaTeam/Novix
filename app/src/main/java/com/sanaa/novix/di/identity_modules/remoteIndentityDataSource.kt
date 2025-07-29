package com.sanaa.novix.di.identity_modules

import com.sanaa.identity.network.AuthenticationApiService
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteIdentityDataSource = module {
    single<AuthenticationApiService> {
        get<Retrofit>().create(AuthenticationApiService::class.java)
    }
}