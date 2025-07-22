package com.sanaa.novix.di.details_modules

import com.sanaa.vod.repository.ActorRepositoryImpl
import com.sanaa.vod.repository.MovieRepositoryImpl
import com.sanaa.vod.repository.TvShowRepositoryImpl
import details.repository.ActorRepository
import details.repository.MovieRepository
import details.repository.TvSeriesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDetailsModule = module {
    singleOf(::ActorRepositoryImpl) bind ActorRepository::class
    singleOf(::MovieRepositoryImpl) bind MovieRepository::class
    singleOf(::TvShowRepositoryImpl) bind TvSeriesRepository::class
}