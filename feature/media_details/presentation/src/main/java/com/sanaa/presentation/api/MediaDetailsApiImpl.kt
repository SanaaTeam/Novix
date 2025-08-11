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

    override fun navigateToTvGenreDetails(context: Context, genreId: Int, genreName: String) {
        val intent = MediaDetailsActivity.createTvGenreIntent(
            context,
            genreId,
            genreName
        )
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as? AppCompatActivity).toBundle()
        )
    }

    override fun navigateToMovieGenreDetails(context: Context, genreId: Int, genreName: String) {
        val intent = MediaDetailsActivity.createMovieGenreIntent(
            context,
            genreId,
            genreName
        )
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as? AppCompatActivity).toBundle()
        )
    }
}

@AndroidEntryPoint
class MediaDetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startRouteString = intent.getStringExtra(EXTRA_START_ROUTE)
        val mediaId = intent.getIntExtra(EXTRA_MEDIA_ID, -1)
        val genreId = intent.getIntExtra(EXTRA_GENRE_ID, -1)
        val genreName = intent.getStringExtra(EXTRA_GENRE_NAME)
        val isTvGenre = intent.getBooleanExtra(EXTRA_IS_TV_GENRE, false)

        val startRoute = when {
            startRouteString != null -> StartRoute.valueOf(startRouteString)
            genreId != -1 && genreName != null -> null
            else -> null
        }

        if (startRoute == null && (genreId == -1 || genreName == null)) {
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
                    DetailsNavHost(
                        startRoute = startRoute,
                        mediaId.takeIf { it != -1 },
                        genreId.takeIf { it != -1 },
                        genreName,
                        isTvGenre
                    )
                }
            }
        }
    }


    companion object {
        private const val EXTRA_START_ROUTE = "extra_start_route"
        private const val EXTRA_MEDIA_ID = "extra_media_id"
        private const val EXTRA_GENRE_ID = "extra_genre_id"
        private const val EXTRA_GENRE_NAME = "extra_genre_name"
        private const val EXTRA_IS_TV_GENRE = "extra_is_tv_genre"


        fun createIntent(context: Context, startRoute: StartRoute, id: Int): Intent {
            return Intent(context, MediaDetailsActivity::class.java).apply {
                putExtra(EXTRA_START_ROUTE, startRoute.name)
                putExtra(EXTRA_MEDIA_ID, id)
            }
        }

        fun createMovieGenreIntent(context: Context, genreId: Int, genreName: String): Intent {
            return Intent(context, MediaDetailsActivity::class.java).apply {
                putExtra(EXTRA_GENRE_ID, genreId)
                putExtra(EXTRA_GENRE_NAME, genreName)
                putExtra(EXTRA_IS_TV_GENRE, false)
            }
        }

        fun createTvGenreIntent(context: Context, genreId: Int, genreName: String): Intent {
            return Intent(context, MediaDetailsActivity::class.java).apply {
                putExtra(EXTRA_GENRE_ID, genreId)
                putExtra(EXTRA_GENRE_NAME, genreName)
                putExtra(EXTRA_IS_TV_GENRE, true)
            }
        }
    }

}