package com.sanaa.novix.di.identity_modules

import com.sanaa.identity.network.AuthenticationApi
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteIdentityDataSource = module {
    single<AuthenticationApi> {
        get<Retrofit>().create(AuthenticationApi::class.java)
    }
}