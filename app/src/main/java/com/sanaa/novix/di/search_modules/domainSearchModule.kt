package com.sanaa.novix.di.search_modules

import org.koin.core.module.dsl.singleOf
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
    singleOf(::SearchMoviesUseCase)
    singleOf(::SearchTvSeriesUseCase)
    singleOf(::SearchActorsUseCase)
    singleOf(::GetRecentViewedUseCase)
    singleOf(::GetSearchHistoryUseCase)
    singleOf(::ClearRecentViewedUseCase)
    singleOf(::ClearSearchHistoryUseCase)
    singleOf(::RemoveSearchHistoryUseCase)
    singleOf(::AddRecentViewedUseCase)
}