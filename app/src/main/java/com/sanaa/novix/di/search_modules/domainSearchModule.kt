package com.sanaa.novix.di.search_modules

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

val domainSearchModule = module {
    single { SearchMoviesUseCase(get(), get()) }
    single { SearchTvSeriesUseCase(get(), get()) }
    single { SearchActorsUseCase(get(), get()) }
    single { GetRecentViewedUseCase(get()) }
    single { GetSearchHistoryUseCase(get()) }
    single { ClearRecentViewedUseCase(get()) }
    single { ClearSearchHistoryUseCase(get()) }
    single { RemoveSearchHistoryUseCase(get()) }
    single { AddRecentViewedUseCase(get()) }
    single { RemoveSearchHistoryUseCase(get()) }
}