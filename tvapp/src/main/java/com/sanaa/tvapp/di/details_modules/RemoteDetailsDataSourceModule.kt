package com.sanaa.tvapp.di.details_modules

import com.sanaa.vod.dataSource.remote.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.RemoteTvShowDataSource
import com.sanaa.vod.media.actor.ActorApiService
import com.sanaa.vod.media.actor.RemoteActorDataSourceImpl
import com.sanaa.vod.media.movie.MovieApiService
import com.sanaa.vod.media.movie.RemoteMovieDataSourceImpl
import com.sanaa.vod.media.tvShow.RemoteTvShowDataSourceImpl
import com.sanaa.vod.media.tvShow.TvShowApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDetailsDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMovieRemoteDataSource(
        dataSource: RemoteMovieDataSourceImpl,
    ): RemoteMovieDataSource

    @Binds
    @Singleton
    abstract fun bindActorRemoteDataSource(
        dataSource: RemoteActorDataSourceImpl,
    ): RemoteActorDataSource

    @Binds
    @Singleton
    abstract fun bindTvShowRemoteDataSource(
        dataSource: RemoteTvShowDataSourceImpl,
    ): RemoteTvShowDataSource

    companion object {

        @Provides
        @Singleton
        fun provideMovieApiService(retrofit: Retrofit): MovieApiService =
            retrofit.create(MovieApiService::class.java)

        @Provides
        @Singleton
        fun provideActorApiService(retrofit: Retrofit): ActorApiService =
            retrofit.create(ActorApiService::class.java)

        @Provides
        @Singleton
        fun provideTvShowApiService(retrofit: Retrofit): TvShowApiService =
            retrofit.create(TvShowApiService::class.java)
    }
}