package com.sanaa.novix

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
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

    private lateinit var  authLuncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        authLuncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            when(result.resultCode){
                AuthenticationApi.RESULT_LOGGED_WITH_SESSION_ID , AuthenticationApi.RESULT_LOGGED_AS_GUEST->{
                    setContent {
                        homeFeatureApi.HomeScreenApi()
                    }
                }
                else -> finish()
            }
        }


        enableEdgeToEdge()

        val sessionId = runBlocking { preferenceManager.sessionId.firstOrNull() }
        if (sessionId.isNullOrEmpty()){
            val authIntent = authenticationApi.getLaunchIntent(this)
            authLuncher.launch(authIntent)
        }else{
            setContent {
                homeFeatureApi.HomeScreenApi()
            }
        }
    }
}