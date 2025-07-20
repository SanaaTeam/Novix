package com.sanaa.novix.di.search_modules

import androidx.room.Room
import com.sanaa.search.dataSource.local.LocalCacheSearchDataSource
import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.search_history.LocalSearchHistoryDataSourceImpl
import com.sanaa.search.search_history.dao.QueryDao
import com.sanaa.search.search_history.dao.RecentViewedDao
import com.sanaa.search.search_result.LocalCachedSearchDataSourceImpl
import com.sanaa.search.search_result.dao.ActorDao
import com.sanaa.search.search_result.dao.MovieDao
import com.sanaa.search.search_result.dao.SearchDao
import com.sanaa.search.search_result.dao.SearchResultDao
import com.sanaa.search.search_result.dao.SeriesDao
import com.sanaa.search.search_result.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "search_db"
        ).build()
    }

    single<SearchDao> { get<AppDatabase>().searchDao() }

    single<SearchResultDao> { get<AppDatabase>().searchResultDao() }

    single<ActorDao> { get<AppDatabase>().actorDao() }

    single<MovieDao> { get<AppDatabase>().movieDao() }

    single<SeriesDao> { get<AppDatabase>().seriesDao() }

    single<QueryDao> { get<AppDatabase>().queryDao() }

    single<RecentViewedDao> { get<AppDatabase>().recentViewedDao() }

    singleOf(::LocalCachedSearchDataSourceImpl) bind LocalCacheSearchDataSource::class

    singleOf(::LocalSearchHistoryDataSourceImpl) bind LocalSearchHistoryDataSource::class
}