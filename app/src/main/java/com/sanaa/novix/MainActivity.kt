package com.sanaa.novix

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sanaa.api.*
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.presentation.app.NovixApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var homeFeatureApi: HomeFeatureApi
    @Inject lateinit var searchFeatureApi: SearchFeatureApi
    @Inject lateinit var mediaDetailsApi: MediaDetailsApi
    @Inject lateinit var authenticationApi: AuthenticationApi
    @Inject lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Timber.d("MainActivity created")

        val sessionId: String? = runBlocking {
            preferencesManager.sessionId.firstOrNull()
        }

        Timber.d("Key: ${BuildConfig.TMDB_API_KEY}")

        setContent {
            NovixApp(
                homeFeatureApi = homeFeatureApi,
                searchFeatureApi = searchFeatureApi,
                mediaDetailsApi = mediaDetailsApi,
                authenticationApi = authenticationApi,
                startWithAuth = TextUtils.isEmpty(sessionId)
            )
        }
    }
}