package com.sanaa.search.data.di

import androidx.room.Room
import org.koin.dsl.module
import com.sanaa.search.data.local.db.AppDatabase
import com.sanaa.search.data.local.dao.SearchDao
import com.sanaa.search.data.local.dao.SearchResultDao
import com.sanaa.search.dataSource.local.LocalCachedSearchDataSource
import com.sanaa.search.data.local.datasource.LocalCachedSearchDataSourceImpl
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

    single<com.sanaa.search.search_history.dao.QueryDao> {
        get<AppDatabase>().queryDao()
    }
    single<com.sanaa.search.search_history.dao.RecentViewedDao> {
        get<AppDatabase>().recentViewedDao()
    }

    single<LocalCachedSearchDataSource> {
        LocalCachedSearchDataSourceImpl(
            searchDao = get<SearchDao>(),
            searchResultDao = get<SearchResultDao>()
        )
    }

} 