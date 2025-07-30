package com.sanaa.novix.di.continue_watch_modules

import com.sanaa.vod.repository.ContinueWatchingRepositoryImpl
import org.koin.dsl.module
import repository.ContinueWatchingRepository

val continueWatchingDomainModule = module {
    single<ContinueWatchingRepository> { ContinueWatchingRepositoryImpl(get(), get()) }
}