package com.sanaa.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var homeFeatureApi: HomeFeatureApi
    @Inject lateinit var authenticationApi: AuthenticationApi
    @Inject lateinit var preferenceManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionId = runBlocking { preferenceManager.sessionId.firstOrNull() }

        setContent {
            if (sessionId.isNullOrEmpty())
                authenticationApi.AuthenticationScreen(this)
            else
                homeFeatureApi.HomeScreenApi()
        }
    }
}