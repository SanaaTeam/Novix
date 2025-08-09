package com.sanaa.tvapp.di

import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.identity.dataSoruce.dataStore.PreferencesManagerImpl
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.presentation.api.AuthenticationApiImpl
import com.sanaa.presentation.api.HomeFeatureApiImpl
import com.sanaa.presentation.api.MediaDetailsApiImpl
import com.sanaa.presentation.api.PlaylistsFeatureApiImpl
import com.sanaa.presentation.api.SearchFeatureApiImpl
import com.sanaa.presentation.api.SearchNavigatorApiImpl
import com.sanaa.presentation.api.UserProfileFeatureApiImpl
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

    @Binds
    @Singleton
    abstract fun bindPlayListFeatureApi(
        playlistsFeatureApiImpl: PlaylistsFeatureApiImpl
    ): PlaylistsFeatureApi

    @Binds
    @Singleton
    abstract fun bindUserProfileApi(
        userProfileApiImpl: UserProfileFeatureApiImpl
    ): UserProfileFeatureApi

//    @Binds
//    @Singleton
//    abstract fun bindPreferencesManager(
//        preferencesManagerImpl: PreferencesManagerImpl
//    ): PreferencesManager

}