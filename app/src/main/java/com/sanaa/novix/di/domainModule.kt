package com.sanaa.novix.di

import details.usecase.ManageActorDetailsUseCase
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import org.koin.dsl.module
import search.usecase.ManageRecentViewedUseCase
import search.usecase.ManageSearchHistoryUseCase
import search.usecase.SearchUseCase

val domainModule = module {
    // search
    single { SearchUseCase(get(), get()) }
    single { ManageSearchHistoryUseCase(get()) }
    single { ManageRecentViewedUseCase(get()) }

    // details
    single { ManageActorDetailsUseCase(get()) }
    single { ManageMovieDetailsUseCase(get()) }
    single { ManageTvSeriesDetailsUseCase(get()) }
    single { ManageEpisodeDetailsUseCase(get()) }
}