package com.sanaa.presentation.api

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.main.DetailsViewModel
import com.sanaa.presentation.navigation.DetailsNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaDetailsApiImpl @Inject constructor() : MediaDetailsApi {
    override fun launch(context: Context, startRoute: StartRoute, id: Int) {
        val intent = MediaDetailsActivity.createIntent(context, startRoute, id)
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as? AppCompatActivity).toBundle()
        )
    }
}

@AndroidEntryPoint
class MediaDetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModels()

    companion object {
        private const val EXTRA_START_ROUTE = "extra_start_route"
        private const val EXTRA_MEDIA_ID = "extra_media_id"

        fun createIntent(context: Context, startRoute: StartRoute, id: Int): Intent {
            return Intent(context, MediaDetailsActivity::class.java).apply {
                putExtra(EXTRA_START_ROUTE, startRoute.name)
                putExtra(EXTRA_MEDIA_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startRoute = intent.getStringExtra(EXTRA_START_ROUTE)?.let { StartRoute.valueOf(it) }
        val mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, -1)

        if (startRoute == null || mediaId == -1) {
            finish()
            return
        }

        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle()
            CompositionLocalProvider(
                LocalThemeProvider provides state.value.isDarkTheme,
                LocalSafeContentThreshold provides state.value.safeContentThreshold
            ) {
                NovixTheme(isDarkMode = state.value.isDarkTheme) {
                    DetailsNavHost(startRoute = startRoute, id = mediaId)
                }
            }
        }
    }
}