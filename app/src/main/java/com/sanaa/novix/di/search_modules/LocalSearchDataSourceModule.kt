package com.sanaa.novix.di.search_modules

import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.LocalHistoryDataSource
import com.sanaa.vod.search.LocalCachedSearchDataSourceImpl
import com.sanaa.vod.history.LocalHistoryDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalSearchDataSourceModule {

    @Binds
    abstract fun bindLocalCachedSearchDataSource(
        localCachedSearchDataSourceImpl: LocalCachedSearchDataSourceImpl
    ): LocalCacheSearchDataSource

    @Binds
    abstract fun bindLocalSearchHistoryDataSource(
        localHistoryDataSourceImpl: LocalHistoryDataSourceImpl
    ): LocalHistoryDataSource
}