package com.sanaa.novix.di

import org.koin.dsl.module
import usecase.AddRecentViewedUseCase
import usecase.ClearRecentViewedUseCase
import usecase.ClearSearchHistoryUseCase
import usecase.GetRecentViewedUseCase
import usecase.GetSearchHistoryUseCase
import usecase.RemoveSearchHistoryUseCase
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase

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