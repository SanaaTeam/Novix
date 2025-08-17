package com.sanaa.presentation.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.screen.actor.ActorScreenViewModel
import com.sanaa.presentation.screen.actor.screen.ActorGalleryScreen
import com.sanaa.presentation.screen.actor.screen.ActorScreen
import com.sanaa.presentation.screen.actor.screen.TopMoviesScreen
import com.sanaa.presentation.screen.actor.screen.TopShowsScreen
import com.sanaa.presentation.screen.episodeDetails.EpisodeDetailsScreen
import com.sanaa.presentation.screen.episodeDetails.EpisodeDetailsScreenViewModel
import com.sanaa.presentation.screen.genreMovies.GenreMoviesScreen
import com.sanaa.presentation.screen.genreMovies.GenreMoviesViewModel
import com.sanaa.presentation.screen.genreTvShows.GenreTvShowsScreen
import com.sanaa.presentation.screen.genreTvShows.GenreTvShowsViewModel
import com.sanaa.presentation.screen.movieDetails.MovieDetailsScreen
import com.sanaa.presentation.screen.movieDetails.MovieDetailsViewModel
import com.sanaa.presentation.screen.review.ReviewScreenViewModel
import com.sanaa.presentation.screen.review.ReviewsScreen
import com.sanaa.presentation.screen.tvShow.TvShowScreen
import com.sanaa.presentation.screen.tvShow.TvShowScreenViewModel

@Composable
fun DetailsNavHost(
    startRoute: StartRoute? = null,
    mediaId: Int? = null,
    genreId: Int? = null,
    genreName: String? = null,
    isTvGenre: Boolean = false,
) {
    val initialRoute = when {
        startRoute != null && mediaId != null -> when (startRoute) {
            StartRoute.TV_SHOW -> TvShowScreenRoute(mediaId)
            StartRoute.MOVIE -> MovieDetailsScreenRoute(mediaId)
            StartRoute.ACTOR -> ActorScreenRoute(mediaId)
        }

        else -> {
            if (isTvGenre) {
                GenreTvShowsScreenRoute(genreId!!, genreName!!)
            } else {
                GenreMovieScreenRoute(genreId!!, genreName!!)
            }
        }
    }
    val navController = rememberNavController()

    val navColor = Theme.colors.surface
    val isDarkTheme = LocalThemeProvider.current
    val view = LocalView.current
    val activity = view.context as? AppCompatActivity
    LaunchedEffect(isDarkTheme) {
        activity?.window?.also { window ->
            window.navigationBarColor = navColor.toArgb()
            WindowInsetsControllerCompat(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }


    CompositionLocalProvider(
        LocalNavControllerProvider provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = initialRoute,
            modifier = Modifier.background(Theme.colors.surface)
        ) {
            composable(route = TvShowScreenRoute::class) {
                val tvShowScreenViewModel: TvShowScreenViewModel = hiltViewModel()
                TvShowScreen(viewModel = tvShowScreenViewModel)
            }

            composable(route = EpisodeDetailsScreenRoute::class) {
                val episodeViewModel: EpisodeDetailsScreenViewModel = hiltViewModel()
                EpisodeDetailsScreen(viewModel = episodeViewModel)
            }

            composable(route = ActorScreenRoute::class) {
                val actorViewModel: ActorScreenViewModel = hiltViewModel()
                ActorScreen(viewModel = actorViewModel)
            }

            composable(route = TopMoviesScreenRoute::class) {
                val actorViewModel: ActorScreenViewModel = hiltViewModel()
                TopMoviesScreen(viewModel = actorViewModel)
            }

            composable(route = TopTvShowsScreenRoute::class) {
                val actorViewModel: ActorScreenViewModel = hiltViewModel()
                TopShowsScreen(viewModel = actorViewModel)
            }

            composable(route = ActorGalleryScreenRoute::class) {
                val actorViewModel: ActorScreenViewModel = hiltViewModel()
                ActorGalleryScreen(
                    viewModel = actorViewModel,
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable(route = ReviewsScreenRoute::class) {
                val reviewScreenViewModel: ReviewScreenViewModel = hiltViewModel()
                ReviewsScreen(viewModel = reviewScreenViewModel)
            }

            composable(route = MovieDetailsScreenRoute::class) {
                val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
                MovieDetailsScreen(viewModel = movieDetailsViewModel)
            }

            composable(route = GenreMovieScreenRoute::class) {
                val genreMoviesViewModel: GenreMoviesViewModel = hiltViewModel()
                GenreMoviesScreen(viewModel = genreMoviesViewModel)
            }

            composable(route = GenreTvShowsScreenRoute::class) {
                val genreTvShowsViewModel: GenreTvShowsViewModel = hiltViewModel()
                GenreTvShowsScreen(viewModel = genreTvShowsViewModel)
            }
        }
    }
}