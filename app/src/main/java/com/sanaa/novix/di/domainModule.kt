package com.sanaa.novix.di

import org.koin.dsl.module
import usecase.AddRecentViewedUseCase
import usecase.SearchActorsUseCase
import usecase.SearchMoviesUseCase
import usecase.SearchTvSeriesUseCase

val domainModule = module {
    factory { SearchMoviesUseCase(get()) }
    factory { SearchTvSeriesUseCase(get()) }
    factory { SearchActorsUseCase(get()) }
    factory { AddRecentViewedUseCase(get()) }

}