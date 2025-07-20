package com.sanaa.novix.di

import details.usecase.ManageActorDetailsUseCase
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import search.usecase.ManageRecentViewedUseCase
import search.usecase.ManageSearchHistoryUseCase
import search.usecase.SearchUseCase

val domainModule = module {
    // search
    singleOf(::SearchUseCase)
    singleOf(::ManageSearchHistoryUseCase)
    singleOf(::ManageRecentViewedUseCase)

    // details
    singleOf(::ManageActorDetailsUseCase)
    singleOf(::ManageMovieDetailsUseCase)
    singleOf(::ManageTvSeriesDetailsUseCase)
    singleOf(::ManageEpisodeDetailsUseCase)
}