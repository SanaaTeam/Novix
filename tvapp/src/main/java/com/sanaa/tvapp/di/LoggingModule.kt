package com.sanaa.tvapp.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sanaa.tvapp.logging.CrashReportingTree
import com.sanaa.tvapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Provides
    @Singleton
    fun provideTimberTree(crashlytics: FirebaseCrashlytics): Timber.Tree {
        return if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashReportingTree(crashlytics)
        }
    }
}