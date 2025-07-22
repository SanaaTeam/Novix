package com.sanaa.novix.di.details_modules

import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.media.actor.ActorApiService
import com.sanaa.vod.media.actor.RemoteActorDataSourceImpl
import com.sanaa.vod.media.movie.MovieApiService
import com.sanaa.vod.media.movie.RemoteMovieDataSourceImp
import com.sanaa.vod.media.tvShow.RemoteTvShowDataSourceImpl
import com.sanaa.vod.media.tvShow.TvShowApiService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDetailsDataSource = module {
    singleOf(::RemoteActorDataSourceImpl) bind RemoteActorDataSource::class
    singleOf(::RemoteMovieDataSourceImp) bind RemoteMovieDataSource::class
    singleOf(::RemoteTvShowDataSourceImpl) bind RemoteTvShowDataSource::class
    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }
    single<ActorApiService> {
        get<Retrofit>().create(ActorApiService::class.java)
    }
    single<TvShowApiService> {
        get<Retrofit>().create(TvShowApiService::class.java)
    }
}