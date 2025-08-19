package com.sanaa.tvapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import repository.UserPreferencesRepository
import timber.log.Timber
import javax.inject.Inject
import org.conscrypt.Conscrypt
import java.security.Security


@HiltAndroidApp
class TvApp: Application() {

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    @Inject
    lateinit var timberTree: Timber.Tree

    @Inject
    lateinit var userPreference: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        val theme = runBlocking { userPreference.getTheme().first() }

        val language = runBlocking { userPreference.getLanguage().first() }

        AppCompatDelegate.setDefaultNightMode(
            if (theme == repository.Theme.DARK) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        val localeList = LocaleListCompat.forLanguageTags(language.code)

        AppCompatDelegate.setApplicationLocales(localeList)
        crashlytics.apply {
            isCrashlyticsCollectionEnabled = true
            setUserId("user_${System.currentTimeMillis()}")
            log("App started")
            setCustomKey("app_start_time", System.currentTimeMillis())
        }
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
    }
}