package com.sanaa.novix.di.continue_watch_modules

import org.koin.dsl.module

val continueWatchModule = module {
    includes(
        continueWatchingUseCaseModule
    )
}