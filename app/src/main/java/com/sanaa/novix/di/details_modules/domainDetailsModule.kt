package com.sanaa.novix.di.details_modules

import details.usecase.ManageActorDetailsUseCase
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageMovieDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import org.koin.dsl.module

val domainDetailsModule = module {
    single { ManageActorDetailsUseCase(get()) }
    single { ManageMovieDetailsUseCase(get()) }
    single { ManageTvSeriesDetailsUseCase(get()) }
    single { ManageEpisodeDetailsUseCase(get()) }
}