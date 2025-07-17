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
import org.koin.dsl.module
import search.repository.SearchHistoryRepository
import search.repository.SearchRepository

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get(), get(), get()) }
    single { SearchRepositoryImpl(get(), get(), get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    single<LocalSearchHistoryDataSource> { LocalSearchHistoryDataSourceImpl(get(), get()) }

    single<ActorRepository> { ActorRepositoryImpl(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get()) }
    single<TvSeriesRepository> { TvSeriesRepositoryImpl(get()) }
    single { ActorRemoteDataSourceImpl(get(), get(), get()) }
    single { MovieDetailsRemoteDataSourceImpl(get(), get(), get()) }
}
