package com.sanaa.search.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanaa.search.data.local.dao.ActorDao
import com.sanaa.search.data.local.dao.MovieDao
import com.sanaa.search.data.local.dao.SeriesDao
import com.sanaa.search.data.local.entity.ActorsLocalDto
import com.sanaa.search.data.local.entity.MoviesLocalDto
import com.sanaa.search.data.local.entity.SearchLocalDto
import com.sanaa.search.data.local.entity.SearchResultLocalDto
import com.sanaa.search.data.local.entity.SeriesLocalDto

@Database(
    entities = [
        SearchLocalDto::class,
        SearchResultLocalDto::class,
        MoviesLocalDto::class,
        SeriesLocalDto::class,
        ActorsLocalDto::class
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
} 