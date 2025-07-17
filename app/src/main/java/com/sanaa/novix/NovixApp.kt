package com.sanaa.novix

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sanaa.novix.di.appModule
import com.sanaa.novix.di.domainModule
import com.sanaa.novix.di.firebaseModule
import com.sanaa.novix.di.loggingModule
import com.sanaa.novix.di.searchModule
import com.sanaa.search.util.TimeUtils
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
            modules(appModule, searchModule, firebaseModule, domainModule, loggingModule)
        }

        val crashlytics: FirebaseCrashlytics = get()
        val tree: Timber.Tree = get()
        val inWholeMilliseconds = TimeUtils.getCurrentTimeStamp()

        if (BuildConfig.DEBUG) {
            Timber.plant(tree, Timber.DebugTree())
        } else {
            Timber.plant(tree)
        }

        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_$inWholeMilliseconds")
            log("App started")
            setCustomKey("app_start_time", inWholeMilliseconds)
        }
    }
}
