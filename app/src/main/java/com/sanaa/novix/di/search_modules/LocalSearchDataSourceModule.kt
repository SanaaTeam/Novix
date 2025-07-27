package com.sanaa.novix.di.search_modules

import com.sanaa.vod.dataSource.local.search.LocalCacheSearchDataSource
import com.sanaa.vod.dataSource.local.search.LocalSearchHistoryDataSource
import com.sanaa.vod.search.search_result.LocalCachedSearchDataSourceImpl
import com.sanaa.vod.search.search_history.LocalSearchHistoryDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalSearchDataSourceModule {

    @Binds
    abstract fun bindLocalCachedSearchDataSource(
        impl: LocalCachedSearchDataSourceImpl
    ): LocalCacheSearchDataSource

    @Binds
    abstract fun bindLocalSearchHistoryDataSource(
        impl: LocalSearchHistoryDataSourceImpl
    ): LocalSearchHistoryDataSource
}
