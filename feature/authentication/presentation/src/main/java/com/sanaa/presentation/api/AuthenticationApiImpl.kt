package com.sanaa.presentation.api

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.AuthStartRoute
import com.sanaa.api.AuthenticationApi
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.navigation.AuthNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationApiImpl @Inject constructor() : AuthenticationApi {
    override fun launch(context: Context, startRoute: AuthStartRoute) {
        val intent = AuthActivity.createIntent(context, startRoute)
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as? AppCompatActivity).toBundle()
        )
    }
}


@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val viewModel: AuthenticationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startRoute =
            intent.getStringExtra(EXTRA_START_ROUTE)?.let { AuthStartRoute.valueOf(it) }

        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle()
            NovixTheme(isDarkMode = state.value.isDarkTheme) {
                AuthNavHost(startRoute = startRoute ?: AuthStartRoute.Welcome)
            }
        }
    }


    internal companion object {
        const val EXTRA_START_ROUTE = "extra_start_route"
        fun createIntent(context: Context, startRoute: AuthStartRoute): Intent {
            return Intent(context, AuthActivity::class.java).apply {
                putExtra(EXTRA_START_ROUTE, startRoute.name)
            }
        }
    }
}