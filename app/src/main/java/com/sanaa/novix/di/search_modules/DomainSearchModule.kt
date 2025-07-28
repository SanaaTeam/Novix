package com.sanaa.novix.di.search_modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.HistoryRepository
import repository.SearchRepository
import usecase.history.ManageHistoryUseCase
import usecase.search.ManageRecentViewedUseCase
import usecase.search.SearchUseCase

@Module
@InstallIn(SingletonComponent::class)
object DomainSearchModule {

    @Provides
    fun provideSearchUseCase(
        searchRepository: SearchRepository,
        searchHistoryRepository: HistoryRepository,
    ): SearchUseCase = SearchUseCase(
            searchRepository,
            searchHistoryRepository
        )

    @Provides
    fun provideManageRecentViewedUseCase(
        historyRepository: HistoryRepository
    ): ManageRecentViewedUseCase =
        ManageRecentViewedUseCase(
            historyRepository
        )

    @Provides
    fun provideManageHistoryUseCase(
        historyRepository: HistoryRepository
    ): ManageHistoryUseCase =
        ManageHistoryUseCase(
            historyRepository
        )
}