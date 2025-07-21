package com.sanaa.novix

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sanaa.novix.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

class NovixApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NovixApp)
            modules(
                appModule,
            )
        }

        val crashlytics: FirebaseCrashlytics = getKoin().get()
        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_${System.currentTimeMillis()}")
            log("App started")
            setCustomKey("app_start_time", System.currentTimeMillis())
        }
    }
}