package com.sanaa.novix.di.details_modules

import usecase.ManageActorUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainDetailsModule = module {
    singleOf(::ManageActorUseCase)
    singleOf(::ManageMovieUseCase)
    singleOf(::ManageTvSeriesUseCase)
    singleOf(::ManageEpisodeDetailsUseCase)
}