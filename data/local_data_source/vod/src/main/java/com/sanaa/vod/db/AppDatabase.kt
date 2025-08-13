package com.sanaa.vod.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanaa.vod.cache.dao.CachedContentDao
import com.sanaa.vod.cache.dao.CachedContentMetadataDao
import com.sanaa.vod.cache.dao.GenreDao
import com.sanaa.vod.cache.dao.MovieDao
import com.sanaa.vod.cache.dao.SavedListMovieDao
import com.sanaa.vod.cache.dao.TvShowDao
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.GenreLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.SavedListMovieDto
import com.sanaa.vod.dataSource.local.cache.dto.TvShowLocalDto
import com.sanaa.vod.dataSource.local.history.dto.search.QueryLocalDto
import com.sanaa.vod.dataSource.local.history.dto.search.RecentViewedLocalDto
import com.sanaa.vod.dataSource.local.history.dto.watchedMedia.WatchedMediaHistoryLocalDto
import com.sanaa.vod.history.dao.QueryDao
import com.sanaa.vod.history.dao.RecentViewedDao
import com.sanaa.vod.history.dao.WatchedMediaHistoryDao

@Database(
    entities = [
        QueryLocalDto::class,
        RecentViewedLocalDto::class,
        WatchedMediaHistoryLocalDto::class,
        CachedContentMetadataLocalDto::class,
        CachedContentLocalDto::class,
        MovieLocalDto::class,
        TvShowLocalDto::class,
        GenreLocalDto::class,
        SavedListMovieDto::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queryDao(): QueryDao
    abstract fun recentViewedDao(): RecentViewedDao
    abstract fun watchedMediaHistoryDao(): WatchedMediaHistoryDao
    abstract fun cachedContentMetadataDao(): CachedContentMetadataDao
    abstract fun cachedContentDao(): CachedContentDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun genreDao(): GenreDao
    abstract fun savedListMovieDao(): SavedListMovieDao
}