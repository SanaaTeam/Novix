package com.sanaa.novix.di

import com.sanaa.actors.ActorRemoteDataSourceImpl
import com.sanaa.actors.repository.ActorRepositoryImpl
import com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
import com.sanaa.movies.repository.MovieRepositoryImpl
import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.repository.SearchHistoryRepositoryImpl
import com.sanaa.search.repository.SearchRepositoryImpl
import com.sanaa.search.search_history.LocalSearchHistoryDataSourceImpl
import com.sanaa.series.TvSeriesRepositoryImpl
import details.repository.ActorRepository
import details.repository.MovieRepository
import details.repository.TvSeriesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import search.repository.SearchHistoryRepository
import search.repository.SearchRepository

val repositoryModule = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::SearchRepositoryImpl)
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
    singleOf(::LocalSearchHistoryDataSourceImpl) bind LocalSearchHistoryDataSource::class

    singleOf(::ActorRepositoryImpl) bind ActorRepository::class
    singleOf(::MovieRepositoryImpl) bind MovieRepository::class
    singleOf(::TvSeriesRepositoryImpl) bind TvSeriesRepository::class
    singleOf(::ActorRemoteDataSourceImpl)
    singleOf(::TvSeriesRepositoryImpl)
    singleOf(::MovieDetailsRemoteDataSourceImpl)
}
