package com.sanaa.tvapp.di.details_modules

import com.sanaa.vod.repository.ActorRepositoryImpl
import com.sanaa.vod.repository.MovieRepositoryImpl
import com.sanaa.vod.repository.TvShowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import repository.ActorRepository
import repository.MovieRepository
import repository.TvShowRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDetailsModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl,
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindActorRepository(
        actorRepositoryImpl: ActorRepositoryImpl,
    ): ActorRepository

    @Binds
    @Singleton
    abstract fun bindTvShowRepository(
        tvShowRepositoryImpl: TvShowRepositoryImpl,
    ): TvShowRepository
}