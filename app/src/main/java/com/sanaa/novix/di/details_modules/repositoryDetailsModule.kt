package com.sanaa.novix.di.details_modules

import com.sanaa.actors.ActorRemoteDataSourceImpl
import com.sanaa.actors.repository.ActorRepositoryImpl
import com.sanaa.movies.repository.MovieRepositoryImpl
import com.sanaa.series.TvSeriesRepositoryImpl
import details.repository.ActorRepository
import details.repository.MovieRepository
import details.repository.TvSeriesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDetailsModule = module {
    singleOf(::ActorRepositoryImpl) bind ActorRepository::class
    singleOf(::MovieRepositoryImpl) bind MovieRepository::class
    singleOf(::TvSeriesRepositoryImpl) bind TvSeriesRepository::class
    singleOf(::ActorRemoteDataSourceImpl)
    singleOf(::TvSeriesRepositoryImpl)
    singleOf(::MovieRepositoryImpl)
}