package com.sanaa.novix.di

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val firebaseModule = module {
    single { Firebase.analytics }
    single { FirebaseCrashlytics.getInstance() }
}