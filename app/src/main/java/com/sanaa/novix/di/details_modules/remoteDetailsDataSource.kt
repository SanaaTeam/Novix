package com.sanaa.novix.di.details_modules

import com.sanaa.actors.ActorRemoteDataSourceImpl
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.movies.MovieApiService
import com.sanaa.movies.MovieDetailsRemoteDataSourceImpl
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.series.RemoteTvSeriesDataSourceImpl
import com.sanaa.series.data_source.remote.RemoteTvSeriesDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDetailsDataSource = module {
    singleOf(::ActorRemoteDataSourceImpl) bind ActorRemoteDataSource::class
    singleOf(::MovieDetailsRemoteDataSourceImpl) bind MovieDetailsRemoteDataSource::class
    singleOf(::RemoteTvSeriesDataSourceImpl) bind RemoteTvSeriesDataSource::class
    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }
}