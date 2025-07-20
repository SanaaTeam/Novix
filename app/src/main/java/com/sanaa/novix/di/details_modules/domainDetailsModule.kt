package com.sanaa.novix.di.details_modules

import details.usecase.ManageActorDetailsUseCase
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainDetailsModule = module {
    singleOf(::ManageActorDetailsUseCase)
    singleOf(::ManageMovieDetailsUseCase)
    singleOf(::ManageTvSeriesDetailsUseCase)
    singleOf(::ManageEpisodeDetailsUseCase)
}