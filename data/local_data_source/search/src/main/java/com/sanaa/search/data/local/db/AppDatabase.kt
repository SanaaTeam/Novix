package com.sanaa.search.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.entity.SearchResultEntity

@Database(
    entities = [SearchResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchResultDao(): SearchResultDao
} 