package com.sanaa.novix

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.analytics.FirebaseAnalytics
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    private val homeFeatureApi: HomeFeatureApi by inject()
    private val authenticationApi: AuthenticationApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        analytics = getKoin().get()

        Timber.d("MainActivity created")
        val preferenceManager: PreferencesManager = getKoin().get()
        val sessionId: String? = runBlocking {
            preferenceManager.sessionId.firstOrNull()
        }
        setContent {
            if (TextUtils.isEmpty(sessionId))
                authenticationApi.AuthenticationScreen(this)
            else
                homeFeatureApi.HomeScreenApi()
        }
    }
}