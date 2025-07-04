package com.sanaa.novix.di

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.sanaa.novix.logging.CrashReportingTree
import org.koin.dsl.module
import timber.log.Timber

val firebaseModule = module {
    single { Firebase.analytics }
    single { FirebaseCrashlytics.getInstance() }
}

val loggingModule = module {
    single<Timber.Tree> { CrashReportingTree(get()) }
}
