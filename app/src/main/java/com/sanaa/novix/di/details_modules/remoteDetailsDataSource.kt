package com.sanaa.novix.di.details_modules

import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvSeriesDataSource
import com.sanaa.vod.media.actor.RemoteActorDataSourceImpl
import com.sanaa.vod.media.movie.MovieApiService
import com.sanaa.vod.media.movie.RemoteMovieDataSourceImp
import com.sanaa.vod.media.tvShow.RemoteTvShowDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDetailsDataSource = module {
    singleOf(::RemoteActorDataSourceImpl) bind RemoteActorDataSource::class
    singleOf(::RemoteMovieDataSourceImp) bind RemoteMovieDataSource::class
    singleOf(::RemoteTvShowDataSourceImpl) bind RemoteTvSeriesDataSource::class
    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }
}