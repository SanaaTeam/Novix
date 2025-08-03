package com.sanaa.novix

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class NovixApp : Application() {
    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    @Inject
    lateinit var timberTree: Timber.Tree

    @Inject

    override fun onCreate() {
        super.onCreate()
        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_${System.currentTimeMillis()}")
            log("App started")
            setCustomKey("app_start_time", System.currentTimeMillis())
        }
    }
}