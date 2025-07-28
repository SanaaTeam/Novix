package com.sanaa.novix.di

import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.api.UserProfileFeatureApi
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

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    abstract fun bindSearchFeatureApi(
        impl: SearchFeatureApiImpl
    ): SearchFeatureApi

    @Binds
    abstract fun bindMediaDetailsApi(
        impl: MediaDetailsApiImpl
    ): MediaDetailsApi

    @Binds
    abstract fun bindHomeFeatureApi(
        impl: HomeFeatureApiImpl
    ): HomeFeatureApi

    @Binds
    abstract fun bindPlaylistsFeatureApi(
        impl: PlaylistsFeatureApiImpl
    ): PlaylistsFeatureApi

    @Binds
    abstract fun bindUserProfileFeatureApi(
        impl: UserProfileFeatureApiImpl
    ): UserProfileFeatureApi

    @Binds
    abstract fun bindSearchNavigatorApi(
        impl: SearchNavigatorApiImpl
    ): SearchNavigatorApi
}
