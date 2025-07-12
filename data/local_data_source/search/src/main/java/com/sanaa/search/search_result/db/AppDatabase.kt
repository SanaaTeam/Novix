package com.sanaa.search.search_result.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto
import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import com.sanaa.search.dataSource.local.dto.RecentViewedLocalDto
import com.sanaa.search.dataSource.local.dto.SearchLocalDto
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.search_history.dao.QueryDao
import com.sanaa.search.search_history.dao.RecentViewedDao
import com.sanaa.search.search_result.dao.ActorDao
import com.sanaa.search.search_result.dao.MovieDao
import com.sanaa.search.search_result.dao.SearchDao
import com.sanaa.search.search_result.dao.SearchResultDao
import com.sanaa.search.search_result.dao.SeriesDao

@Database(
    entities = [
        SearchLocalDto::class,
        SearchResultLocalDto::class,
        MoviesLocalDto::class,
        TvSeriesLocalDto::class,
        ActorsLocalDto::class,
        QueryLocalDto::class,
        RecentViewedLocalDto::class
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
} 