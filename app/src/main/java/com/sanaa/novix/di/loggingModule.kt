package com.sanaa.novix.di

import com.sanaa.novix.logging.CrashReportingTree
import org.koin.dsl.module
import timber.log.Timber

val loggingModule = module {
    single<Timber.Tree> { CrashReportingTree(get()) }
}