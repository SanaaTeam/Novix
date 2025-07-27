package com.sanaa.novix.di.details_modules

import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.tvShow.RemoteTvShowDataSource
import com.sanaa.vod.media.actor.ActorApiService
import com.sanaa.vod.media.actor.RemoteActorDataSourceImpl
import com.sanaa.vod.media.movie.MovieApiService
import com.sanaa.vod.media.movie.RemoteMovieDataSourceImpl
import com.sanaa.vod.media.tvShow.RemoteTvShowDataSourceImpl
import com.sanaa.vod.media.tvShow.TvShowApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RemoteDetailsDataSourceModule {

    @Provides
    fun provideMovieApiService(
        retrofit: Retrofit
    ): MovieApiService = retrofit.create(MovieApiService::class.java)

    @Provides
    fun provideRemoteMovieDataSource(
        api: MovieApiService
    ): RemoteMovieDataSource = RemoteMovieDataSourceImpl(api)

    @Provides
    fun provideActorApiService(
        retrofit: Retrofit
    ): ActorApiService = retrofit.create(ActorApiService::class.java)

    @Provides
    fun provideRemoteActorDataSource(
        api: ActorApiService
    ): RemoteActorDataSource = RemoteActorDataSourceImpl(api)

    @Provides
    fun provideTvShowApiService(
        retrofit: Retrofit
    ): TvShowApiService = retrofit.create(TvShowApiService::class.java)

    @Provides
    fun provideRemoteTvShowDataSource(
        api: TvShowApiService
    ): RemoteTvShowDataSource = RemoteTvShowDataSourceImpl(api)
}