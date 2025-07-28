package com.sanaa.novix.di.details_modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.ActorRepository
import repository.MovieRepository
import repository.TvSeriesRepository
import usecase.ManageActorUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase

@Module
@InstallIn(SingletonComponent::class)
object DomainDetailsModule {

    @Provides
    fun provideManageActorUseCase(
        actorRepository: ActorRepository
    ): ManageActorUseCase = ManageActorUseCase(actorRepository)

    @Provides
    fun provideManageMovieUseCase(
        movieRepository: MovieRepository
    ): ManageMovieUseCase = ManageMovieUseCase(movieRepository)

    @Provides
    fun provideManageTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): ManageTvSeriesUseCase = ManageTvSeriesUseCase(tvSeriesRepository)

    @Provides
    fun provideManageEpisodeDetailsUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): ManageEpisodeDetailsUseCase = ManageEpisodeDetailsUseCase(tvSeriesRepository)
}