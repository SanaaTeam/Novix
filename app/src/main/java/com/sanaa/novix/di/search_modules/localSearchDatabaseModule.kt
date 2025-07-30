package com.sanaa.novix.di.search_modules

import android.content.Context
import androidx.room.Room
import com.sanaa.vod.search.search_history.dao.QueryDao
import com.sanaa.vod.search.search_history.dao.RecentViewedDao
import com.sanaa.vod.search.search_result.dao.ActorDao
import com.sanaa.vod.search.search_result.dao.MovieDao
import com.sanaa.vod.search.search_result.dao.SearchDao
import com.sanaa.vod.search.search_result.dao.SearchResultDao
import com.sanaa.vod.search.search_result.dao.SeriesDao
import com.sanaa.vod.search.search_result.db.AppDatabase
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
    fun provideSearchDao(database: AppDatabase): SearchDao =
        database.searchDao()

    @Provides
    fun provideSearchResultDao(database: AppDatabase): SearchResultDao =
        database.searchResultDao()

    @Provides
    fun provideActorDao(database: AppDatabase): ActorDao =
        database.actorDao()

    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao =
        database.movieDao()

    @Provides
    fun provideSeriesDao(database: AppDatabase): SeriesDao =
        database.seriesDao()

    @Provides
    fun provideQueryDao(database: AppDatabase): QueryDao =
        database.queryDao()

    @Provides
    fun provideRecentViewedDao(database: AppDatabase): RecentViewedDao =
        database.recentViewedDao()
}