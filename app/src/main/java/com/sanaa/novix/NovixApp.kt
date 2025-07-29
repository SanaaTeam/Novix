package com.sanaa.novix

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class NovixApp : Application() {
    @Inject lateinit var crashlytics: FirebaseCrashlytics
    @Inject lateinit var timberTree: Timber.Tree

    override fun onCreate() {
        super.onCreate()

        Timber.plant(timberTree)

        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_${System.currentTimeMillis()}")
            log("App started")
            setCustomKey("app_start_time", System.currentTimeMillis())
        }
    }
}