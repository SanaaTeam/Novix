package com.sanaa.novix

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sanaa.novix.di.appModule
import com.sanaa.novix.di.searchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import timber.log.Timber

class NovixApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NovixApp)
            modules(appModule, searchModule)
        }

        val crashlytics: FirebaseCrashlytics = get()
        val tree: Timber.Tree = get()

        if (BuildConfig.DEBUG) {
            Timber.plant(tree, Timber.DebugTree())
        } else {
            Timber.plant(tree)
        }

        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_${System.currentTimeMillis()}")
            log("App started")
            setCustomKey("app_start_time", System.currentTimeMillis())
        }
    }
}
