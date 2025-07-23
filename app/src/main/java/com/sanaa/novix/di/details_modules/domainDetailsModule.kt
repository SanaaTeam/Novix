package com.sanaa.novix.di.details_modules

import details.usecase.ManageActorUseCase
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageMovieUseCase
import details.usecase.ManageTvSeriesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainDetailsModule = module {
    singleOf(::ManageActorUseCase)
    singleOf(::ManageMovieUseCase)
    singleOf(::ManageTvSeriesUseCase)
    singleOf(::ManageEpisodeDetailsUseCase)
}