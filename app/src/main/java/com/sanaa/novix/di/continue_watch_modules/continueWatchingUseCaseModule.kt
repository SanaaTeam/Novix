package com.sanaa.novix.di.continue_watch_modules

import org.koin.dsl.module
import usecase.continueWatch.ManageContinueWatchingUseCase

val continueWatchingUseCaseModule = module {
    factory { ManageContinueWatchingUseCase(get()) }
}