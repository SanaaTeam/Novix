package com.sanaa.vod.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanaa.vod.continueWatch.dao.WatchedMediaHistoryDao
import com.sanaa.vod.dataSource.local.continueWatch.dto.WatchedMediaHistoryLocalDto
import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.QueryLocalDto
import com.sanaa.vod.dataSource.local.search.dto.RecentViewedLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchLocalDto
import com.sanaa.vod.dataSource.local.search.dto.SearchResultLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.search.search_history.dao.QueryDao
import com.sanaa.vod.search.search_history.dao.RecentViewedDao
import com.sanaa.vod.search.search_result.dao.ActorDao
import com.sanaa.vod.search.search_result.dao.MovieDao
import com.sanaa.vod.search.search_result.dao.SearchDao
import com.sanaa.vod.search.search_result.dao.SearchResultDao
import com.sanaa.vod.search.search_result.dao.SeriesDao

@Database(
    entities = [
        SearchLocalDto::class,
        SearchResultLocalDto::class,
        MovieLocalDto::class,
        TvSeriesLocalDto::class,
        ActorLocalDto::class,
        QueryLocalDto::class,
        RecentViewedLocalDto::class,
        WatchedMediaHistoryLocalDto::class,

    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
    abstract fun searchResultDao(): SearchResultDao
    abstract fun movieDao(): MovieDao
    abstract fun seriesDao(): SeriesDao
    abstract fun actorDao(): ActorDao
    abstract fun queryDao(): QueryDao
    abstract fun recentViewedDao(): RecentViewedDao
    abstract fun watchedMediaHistoryDao(): WatchedMediaHistoryDao
}