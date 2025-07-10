package com.sanaa.search.data.di

import androidx.room.Room
import org.koin.dsl.module
import com.sanaa.search.data.local.db.AppDatabase
import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.data.local.datasource.LocalSearchDataSource
import org.koin.android.ext.koin.androidContext

val localDatabaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "search_db"
        ).build()
    }

    single<SearchDao> {
        get<AppDatabase>().searchDao()
    }

    single<SearchResultDao> {
        get<AppDatabase>().searchResultDao()
    }

} 