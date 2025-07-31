package com.sanaa.novix.di.continue_watch_modules

import org.koin.dsl.module
import repository.ContinueWatchingRepository

val continueWatchingDomainModule = module {
    single<ContinueWatchingRepository> { ContinueWatchingRepositoryImpl(get(), get()) }
}