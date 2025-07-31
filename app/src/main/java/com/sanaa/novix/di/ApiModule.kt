package com.sanaa.novix.di

import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.presentation.api.AuthenticationApiImpl
import com.sanaa.presentation.api.HomeFeatureApiImpl
import com.sanaa.presentation.api.MediaDetailsApiImpl
import com.sanaa.presentation.api.SearchFeatureApiImpl
import com.sanaa.presentation.api.SearchNavigatorApiImpl
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
    abstract fun bindAuthenticationApi(
        authenticationApiImpl: AuthenticationApiImpl
    ): AuthenticationApi

    @Binds
    @Singleton
    abstract fun bindSearchFeatureApi(
        searchFeatureApiImpl: SearchFeatureApiImpl
    ): SearchFeatureApi

    @Binds
    @Singleton
    abstract fun bindMediaDetailsApi(
        mediaDetailsApiImpl: MediaDetailsApiImpl
    ): MediaDetailsApi

    @Binds
    @Singleton
    abstract fun bindHomeFeatureApi(
        homeFeatureApiImpl: HomeFeatureApiImpl
    ): HomeFeatureApi

    @Binds
    @Singleton
    abstract fun bindSearchNavigatorApi(
        searchNavigatorApiImpl: SearchNavigatorApiImpl
    ): SearchNavigatorApi
}