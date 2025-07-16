package com.sanaa.novix.di

import details.usecase.actor.GetActorDetailsUseCase
import details.usecase.actor.GetActorTopMoviesUseCase
import details.usecase.actor.GetActorTopTvSeriesUseCase
import details.usecase.actor.GetGalleryImagesUseCase
import details.usecase.actor.GetProfileImagesUseCase
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

    single { GetActorDetailsUseCase(get()) }
    single { GetActorTopMoviesUseCase(get()) }
    single { GetActorTopTvSeriesUseCase(get()) }
    single { GetGalleryImagesUseCase(get()) }
    single { GetProfileImagesUseCase(get()) }
}