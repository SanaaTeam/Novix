package com.sanaa.tvapp.di.apiModule

import com.sanaa.tvapp.presentation.screens.login.LoginApiImpl
import com.sanaa.tvapp.presentation.screens.login.api.LoginApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    @Singleton
    abstract fun bindLoginApi(
        loginApiImpl: LoginApiImpl
    ): LoginApi

}