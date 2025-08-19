package com.sanaa.novix.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sanaa.novix.logging.CrashReportingTree
import com.sanaa.novix.BuildConfig
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