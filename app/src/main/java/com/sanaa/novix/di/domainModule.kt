package com.sanaa.novix.di

import details.usecase.ManageActorDetailsUseCase
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
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
    // search
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

    // details
    single { ManageActorDetailsUseCase(get()) }
    single { ManageMovieDetailsUseCase(get()) }
    single { ManageTvSeriesDetailsUseCase(get()) }
    single { ManageEpisodeDetailsUseCase(get()) }
}