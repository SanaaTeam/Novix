package com.sanaa.novix.di.details_modules

import com.sanaa.actors.ActorRemoteDataSourceImpl
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.series.RemoteTvSeriesDataSourceImpl
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import org.koin.dsl.module

val remoteDetailsDataSource = module {
    single<ActorRemoteDataSource> {
        ActorRemoteDataSourceImpl(get(), get())
    }
    single<MovieDetailsRemoteDataSource> {
        MovieDetailsRemoteDataSourceImpl(get(), get())
    }
    single<RemoteTvSeriesDataSource> {
        RemoteTvSeriesDataSourceImpl(get(), get())
    }
}