package com.sanaa.novix.di

import org.koin.dsl.module
import search.usecase.AddRecentViewedUseCase
import search.usecase.ClearRecentViewedUseCase
import search.usecase.ClearSearchHistoryUseCase
import search.usecase.GetRecentViewedUseCase
import search.usecase.GetSearchHistoryUseCase
import search.usecase.RemoveSearchHistoryUseCase
import search.usecase.SearchActorsUseCase
import search.usecase.SearchMoviesUseCase
import search.usecase.SearchTvSeriesUseCase

val domainModule = module {
    factory { SearchMoviesUseCase(get(), get()) }
    factory { SearchTvSeriesUseCase(get(), get()) }
    factory { SearchActorsUseCase(get(), get()) }
    factory { GetRecentViewedUseCase(get()) }
    factory { GetSearchHistoryUseCase(get()) }
    factory { ClearRecentViewedUseCase(get()) }
    factory { ClearSearchHistoryUseCase(get()) }
    factory { RemoveSearchHistoryUseCase(get()) }
    factory { AddRecentViewedUseCase(get()) }
    factory { RemoveSearchHistoryUseCase(get()) }
}