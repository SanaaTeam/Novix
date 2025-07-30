package com.sanaa.novix

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sanaa.api.*
import com.sanaa.presentation.app.NovixApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    @Inject
    lateinit var searchFeatureApi: SearchFeatureApi

    @Inject
    lateinit var mediaDetailsApi: MediaDetailsApi

    private val authenticationApi: AuthenticationApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Timber.d("MainActivity created")
        val preferenceManager: PreferencesManager = getKoin().get()
        val sessionId: String? = runBlocking {
            preferenceManager.sessionId.firstOrNull()
        }
        Log.d("API_KEY_CHECK", "Key: ${BuildConfig.TMDB_API_KEY}")
        setContent {
            NovixApp(
                homeFeatureApi = homeFeatureApi,
                searchFeatureApi = searchFeatureApi,
                mediaDetailsApi = mediaDetailsApi
            )
        }

        setContent {
            if (TextUtils.isEmpty(sessionId))
                authenticationApi.AuthenticationScreen(this)
            else
                homeFeatureApi.HomeScreenApi()
        }
    }
}