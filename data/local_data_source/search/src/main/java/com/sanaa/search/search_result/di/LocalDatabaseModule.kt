package com.sanaa.search.search_result.di

import androidx.room.Room
import com.example.preferences.service.LanguageProvider
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.search_result.dao.ActorDao
import com.sanaa.search.search_result.dao.MovieDao
import com.sanaa.search.search_result.dao.SearchDao
import com.sanaa.search.search_result.dao.SearchResultDao
import com.sanaa.search.search_result.dao.SeriesDao
import com.sanaa.search.search_result.LocalCachedSearchDataSourceImpl
import com.sanaa.search.search_result.db.AppDatabase
import com.sanaa.search.search_history.dao.QueryDao
import com.sanaa.search.search_history.dao.RecentViewedDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

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

    single<ActorDao> {
        get<AppDatabase>().actorDao()
    }

    single<MovieDao> {
        get<AppDatabase>().movieDao()
    }

    single<SeriesDao> {
        get<AppDatabase>().seriesDao()
    }

    single<QueryDao> {
        get<AppDatabase>().queryDao()
    }

    single<RecentViewedDao> {
        get<AppDatabase>().recentViewedDao()
    }

    single<LocalCacheSearchDataSource> {
        LocalCachedSearchDataSourceImpl(
            searchDao = get<SearchDao>(),
            searchResultDao = get<SearchResultDao>(),
            actorDao = get<ActorDao>(),
            movieDao = get<MovieDao>(),
            seriesDao = get<SeriesDao>(),
            languageProvider = get<LanguageProvider>(),
        )
    }

} 