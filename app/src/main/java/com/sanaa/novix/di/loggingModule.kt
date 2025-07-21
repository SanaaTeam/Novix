package com.sanaa.novix.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.sanaa.novix.logging.CrashReportingTree
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

val loggingModule = module {

    single<FirebaseCrashlytics> { Firebase.crashlytics }

    single<Timber.Tree>(named("crash")) { CrashReportingTree(get()) }

    if (BuildConfig.DEBUG) {
        single<Timber.Tree>(named("debug")) { Timber.DebugTree() }
    }

    single(createdAtStart = true) {
        object {
            init {
                getKoin().getAll<Timber.Tree>().forEach(Timber::plant)
            }
        }
    }
}
