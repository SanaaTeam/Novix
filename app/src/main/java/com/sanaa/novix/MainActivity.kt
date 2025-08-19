package com.sanaa.novix

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.sanaa.api.AuthStartRoute
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.OnboardingApi
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.novix.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    @Inject
    lateinit var authenticationApi: AuthenticationApi

    @Inject
    lateinit var onboardingApi: OnboardingApi

    @Inject
    lateinit var preferenceManager: PreferencesManager

    private val viewModel: MainViewModel by viewModels()

    private lateinit var onboardingLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            !viewModel.state.value.isReady
        }

        registerLaunchers()

        lifecycleScope.launch {
            viewModel.state.first { it.isReady }

            if (preferenceManager.isFirstLaunch.first()) {
                onboardingLauncher.launch(onboardingApi.getLaunchIntent(this@MainActivity))
            } else {
                handleAuthOrMain()
            }
        }
    }

    private fun registerLaunchers() {
        onboardingLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        preferenceManager.disableFirstLaunch()
                        handleAuthOrMain()
                    }
                } else {
                    finish()
                }
            }

    }

    private suspend fun handleAuthOrMain() {
        val sessionId = preferenceManager.sessionId.firstOrNull()
        if (sessionId.isNullOrEmpty()) {
            authenticationApi.launch(this, AuthStartRoute.Welcome)
        } else {
            setMainContent()
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val sessionId = preferenceManager.sessionId.firstOrNull()
            if (!sessionId.isNullOrEmpty()) {
                setMainContent()
            }
        }
    }

    private fun setMainContent() {
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle()
            NovixTheme(isDarkMode = state.value.isDarkTheme) {
                homeFeatureApi.HomeScreenApi()
            }
        }
    }
}