package com.sanaa.novix.di.search_modules

import android.content.Context
import androidx.room.Room
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.cache.dao.MovieDao
import com.sanaa.vod.cache.dao.TvShowDao
import com.sanaa.vod.db.AppDatabase
import com.sanaa.vod.history.dao.QueryDao
import com.sanaa.vod.history.dao.RecentViewedDao
import com.sanaa.vod.history.dao.WatchedMediaHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalSearchDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "search_db")
            .build()

    @Provides
    fun provideQueryDao(database: AppDatabase): QueryDao =
        database.queryDao()

    @Provides
    fun provideRecentViewedDao(database: AppDatabase): RecentViewedDao =
        database.recentViewedDao()

    @Provides
    fun provideWatchedMediaHistoryDao(database: AppDatabase): WatchedMediaHistoryDao =
        database.watchedMediaHistoryDao()

    @Provides
    fun provideCachedContentDao(database: AppDatabase): CachedContentDao =
        database.cachedContentDao()

    @Provides
    fun provideCachedContentMetadataDao(database: AppDatabase): CachedContentMetadataDao =
        database.cachedContentMetadataDao()

    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao =
        database.movieDao()

    @Provides
    fun provideTvShowDao(database: AppDatabase): TvShowDao =
        database.tvShowDao()
}