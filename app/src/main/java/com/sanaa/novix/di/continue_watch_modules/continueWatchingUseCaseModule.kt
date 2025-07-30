package com.sanaa.novix.di.continue_watch_modules

import org.koin.dsl.module
import usecase.history.ManageWatchedMediaHistoryUseCase

val continueWatchingUseCaseModule = module {
    factory { ManageWatchedMediaHistoryUseCase(get()) }
}