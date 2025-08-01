package com.sanaa.novix.di.continue_watch_modules

import com.sanaa.vod.continueWatch.dao.WatchedMediaHistoryDao
import com.sanaa.vod.db.AppDatabase
import org.koin.dsl.module

val localContinueWatchingModule = module {
    single<WatchedMediaHistoryDao> { get<AppDatabase>().watchedMediaHistoryDao() }
}