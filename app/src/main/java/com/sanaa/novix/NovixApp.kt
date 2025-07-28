package com.sanaa.novix

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class NovixApp : Application() {
    @Inject
    lateinit var crashlytics: FirebaseCrashlytics
    @Inject
    lateinit var timberTree: Timber.Tree
    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(timberTree, Timber.DebugTree())
        else
            Timber.plant(timberTree)

        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_${System.currentTimeMillis()}")
            log("App started")
            setCustomKey("app_start_time", System.currentTimeMillis())
        }
    }
}