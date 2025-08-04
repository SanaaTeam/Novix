package com.sanaa.novix.di.search_modules

import com.sanaa.vod.cache.DailyCachedContentDataSourceImpl
import com.sanaa.vod.dataSource.local.cache.DailyCachedContentDataSource
import com.sanaa.vod.dataSource.local.history.LocalHistoryDataSource
import com.sanaa.vod.history.LocalHistoryDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalHistoryDataSourceModule {

    @Binds
    abstract fun bindLocalHistoryDataSource(
        localHistoryDataSourceImpl: LocalHistoryDataSourceImpl
    ): LocalHistoryDataSource

    @Binds
    abstract fun bindDailyCachedContentDataSource(
        dailyCachedContentDataSource: DailyCachedContentDataSourceImpl
    ): DailyCachedContentDataSource
}