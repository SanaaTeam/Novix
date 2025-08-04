package com.sanaa.vod.db

import androidx.room.Database
import androidx.room.RoomDatabase
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
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queryDao(): QueryDao
    abstract fun recentViewedDao(): RecentViewedDao
    abstract fun watchedMediaHistoryDao(): WatchedMediaHistoryDao
}