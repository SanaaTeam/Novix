package com.sanaa.novix.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.sanaa.novix.logging.CrashReportingTree
import org.koin.dsl.module
import timber.log.Timber
import com.sanaa.novix.BuildConfig

val loggingModule = module {

    single<FirebaseCrashlytics> { Firebase.crashlytics }

    single<Timber.Tree> {
        if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashReportingTree(get())
        }
    }

    single(createdAtStart = true) {
        object {
            init {
                Timber.plant(get())
            }
        }
    }
}
