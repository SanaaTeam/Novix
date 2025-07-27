package com.sanaa.novix.di.search_modules

import com.sanaa.vod.repository.SearchHistoryRepositoryImpl
import com.sanaa.vod.repository.SearchRepositoryImpl
import repository.HistoryRepository
import repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositorySearchModule {

    @Binds
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindHistoryRepository(
        impl: SearchHistoryRepositoryImpl
    ): HistoryRepository
}
