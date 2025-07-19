package com.sanaa.movies.di

import com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import org.koin.dsl.module


val remoteMovieDetailsDataSource = module {
    single<MovieDetailsRemoteDataSource> { MovieDetailsRemoteDataSourceImpl(get(), get()) }
}