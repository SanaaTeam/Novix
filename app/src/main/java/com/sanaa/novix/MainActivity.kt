package com.sanaa.novix

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.sanaa.api.AuthStartRoute
import com.sanaa.api.AuthenticationApi
import com.sanaa.api.HomeFeatureApi
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    @Inject
    lateinit var authenticationApi: AuthenticationApi

    @Inject
    lateinit var preferenceManager: PreferencesManager

    @Inject
    lateinit var userPreference: repository.UserPreference

    private lateinit var authLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        applyAppLanguage()

        authLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                AuthenticationApi.RESULT_LOGGED_WITH_SESSION_ID,
                AuthenticationApi.RESULT_LOGGED_AS_GUEST -> {
                    setContent { homeFeatureApi.HomeScreenApi() }
                }

                else -> finish()
            }
        }

        enableEdgeToEdge()


        val sessionId = runBlocking { preferenceManager.sessionId.firstOrNull() }
        if (sessionId.isNullOrEmpty()) {
            val authIntent = authenticationApi.getLaunchIntent(this, AuthStartRoute.Welcome)
            authLauncher.launch(authIntent)
        } else {
            setContent { homeFeatureApi.HomeScreenApi() }
        }
    }


    private fun applyAppLanguage() {
        lifecycleScope.launch {
            userPreference.getLanguage().collect { language ->
                val langCode = language.code
                Locale.setDefault(Locale(langCode))
                val localeList = LocaleListCompat.forLanguageTags(langCode)
                val currentLocales = AppCompatDelegate.getApplicationLocales()
                if (currentLocales != localeList) {
                    AppCompatDelegate.setApplicationLocales(localeList)
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        recreate()
                    }
                }
            }
        }
    }
}