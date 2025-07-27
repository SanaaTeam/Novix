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
    fun provideAppDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "search_db")
            .build()

    @Provides
    fun provideSearchDao(db: AppDatabase): SearchDao = db.searchDao()

    @Provides
    fun provideSearchResultDao(db: AppDatabase): SearchResultDao = db.searchResultDao()

    @Provides
    fun provideActorDao(db: AppDatabase): ActorDao = db.actorDao()

    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()

    @Provides
    fun provideSeriesDao(db: AppDatabase): SeriesDao = db.seriesDao()

    @Provides
    fun provideQueryDao(db: AppDatabase): QueryDao = db.queryDao()

    @Provides
    fun provideRecentViewedDao(db: AppDatabase): RecentViewedDao = db.recentViewedDao()

}