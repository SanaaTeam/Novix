package com.sanaa.novix.di.details_modules

import com.sanaa.vod.repository.ActorRepositoryImpl
import com.sanaa.vod.repository.MovieRepositoryImpl
import com.sanaa.vod.repository.TvShowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import repository.ActorRepository
import repository.MovieRepository
import repository.TvSeriesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDetailsModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindActorRepository(
        impl: ActorRepositoryImpl
    ): ActorRepository

    @Binds
    @Singleton
    abstract fun bindTvSeriesRepository(
        impl: TvShowRepositoryImpl
    ): TvSeriesRepository
}
