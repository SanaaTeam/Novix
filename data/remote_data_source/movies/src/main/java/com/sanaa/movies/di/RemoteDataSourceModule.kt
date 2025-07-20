package com.sanaa.movies.di

import com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val remoteMovieDetailsDataSource = module {
    singleOf(::MovieDetailsRemoteDataSourceImpl)
}