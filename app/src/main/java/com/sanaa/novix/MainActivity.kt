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
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.novix.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
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
    lateinit var preferenceManager: PreferencesManager

    private val viewModel: MainViewModel by viewModels()

    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            !viewModel.state.value.isReady
        }

        authLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AuthenticationApi.RESULT_LOGGED_WITH_SESSION_ID,
                    AuthenticationApi.RESULT_LOGGED_AS_GUEST -> {
                        setMainContent()
                    }

                    else -> finish()
                }
            }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.isReady) {
                    val sessionId = preferenceManager.sessionId.firstOrNull()
                    if (sessionId.isNullOrEmpty()) {
                        val authIntent = authenticationApi.getLaunchIntent(
                            this@MainActivity,
                            AuthStartRoute.Welcome
                        )
                        authLauncher.launch(authIntent)
                    } else {
                        setMainContent()
                    }
                }
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