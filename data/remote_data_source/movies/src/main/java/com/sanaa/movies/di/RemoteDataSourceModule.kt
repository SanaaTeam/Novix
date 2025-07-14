package com.sanaa.movies.di

import com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
import com.sanaa.movies.dataSource.MovieDetailsRemoteDataSource
import org.koin.dsl.module


val remoteDataSource = module {
    single<MovieDetailsRemoteDataSource> { MovieDetailsRemoteDataSourceImpl(get(), get(),get()) }
}