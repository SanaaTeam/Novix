package com.sanaa.novix.di.continue_watch_modules

import com.sanaa.vod.continueWatch.LocalContinueWatchingDataSourceImpl
import com.sanaa.vod.dataSource.local.continueWatch.LocalContinueWatchingDataSource
import com.sanaa.vod.db.AppDatabase
import org.koin.dsl.module

val continueWatchingDataModule = module {
    single { get<AppDatabase>().continueWatchingDao() }

    single<LocalContinueWatchingDataSource> {
        LocalContinueWatchingDataSourceImpl(get(), get(), get())
    }
}