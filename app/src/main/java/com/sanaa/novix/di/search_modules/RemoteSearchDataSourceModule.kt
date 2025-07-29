package com.sanaa.novix.di.search_modules

import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource
import com.sanaa.vod.search.SearchApiService
import com.sanaa.vod.search.SearchRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteSearchDataSourceModule {

    @Binds
    abstract fun bindSearchRemoteDataSource(
        searchRemoteDataSourceImplementation: SearchRemoteDataSourceImpl
    ): SearchRemoteDataSource

    companion object {

        @Provides
        @Singleton
        fun provideSearchApiService(retrofit: Retrofit): SearchApiService =
            retrofit.create(SearchApiService::class.java)
    }
}