package com.sanaa.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.actor.ActorViewModel
import com.sanaa.presentation.screen.actor.screen.ActorGalleryScreen
import com.sanaa.presentation.screen.actor.screen.ActorScreen
import com.sanaa.presentation.screen.actor.screen.TopMoviesScreen
import com.sanaa.presentation.screen.actor.screen.TopSeriesScreen
import com.sanaa.presentation.screen.episodeDetails.EpisodeDetailsScreen
import com.sanaa.presentation.screen.episodeDetails.EpisodeDetailsScreenViewModel
import com.sanaa.presentation.screen.genreMovies.GenreMoviesScreen
import com.sanaa.presentation.screen.genreMovies.GenreMoviesViewModel
import com.sanaa.presentation.screen.genreTvShows.GenreTvShowsScreen
import com.sanaa.presentation.screen.genreTvShows.GenreTvShowsViewModel
import com.sanaa.presentation.screen.movieDetails.MovieDetailsScreen
import com.sanaa.presentation.screen.movieDetails.MovieDetailsViewModel
import com.sanaa.presentation.screen.review.ReviewViewModel
import com.sanaa.presentation.screen.review.ReviewsScreen
import com.sanaa.presentation.screen.series.SeriesScreen
import com.sanaa.presentation.screen.series.SeriesViewModel

@Composable
fun DetailsNavHost(startRoute: StartRoute, id: Int) {
    val initialRoute = when (startRoute) {
        StartRoute.SERIES -> SeriesDetailsScreenRoute(id).route()
        StartRoute.MOVIE -> MovieDetailsScreenRoute(id).route()
        StartRoute.ACTOR -> ActorDetailsScreenRoute(id).route()
    }

    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavControllerProvider provides navController
    ) {
        NovixTheme(isDarkMode = isSystemInDarkTheme()) {
            NavHost(
                navController = navController,
                startDestination = initialRoute,
                modifier = Modifier.background(Theme.colors.surface)
            ) {

                // ──── Series ───────────────────────────────────────────────────────────
                composable(
                    route = SeriesDetailsScreenRoute.PATTERN,
                    arguments = listOf(
                        navArgument(SeriesDetailsScreenRoute.ARG_SERIES_ID) {
                            type = NavType.IntType
                        }
                    )
                ) {
                    val seriesViewModel: SeriesViewModel = hiltViewModel()
                    SeriesScreen(viewModel = seriesViewModel)
                }

                // ──── Episodes ─────────────────────────────────────────────────────────
                composable(
                    route = EpisodeDetailsScreenRoute.PATTERN,
                    arguments = listOf(
                        navArgument(EpisodeDetailsScreenRoute.ARG_SERIES_ID) {
                            type = NavType.IntType
                        },
                        navArgument(EpisodeDetailsScreenRoute.ARG_SEASON_NUMBER) {
                            type = NavType.IntType
                        },
                        navArgument(EpisodeDetailsScreenRoute.ARG_EPISODE_NUMBER) {
                            type = NavType.IntType
                        }
                    )
                ) {
                    val episodeViewModel: EpisodeDetailsScreenViewModel = hiltViewModel()
                    EpisodeDetailsScreen(viewModel = episodeViewModel)
                }

                // ──── Actors ───────────────────────────────────────────────────────────
                composable(
                    route = ActorDetailsScreenRoute.PATTERN,
                    arguments = listOf(navArgument(ActorDetailsScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) {
                    val actorViewModel: ActorViewModel = hiltViewModel()
                    ActorScreen(viewModel = actorViewModel)
                }

                composable(
                    route = TopMoviesScreenRoute.PATTERN,
                    arguments = listOf(navArgument(TopMoviesScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) {
                    val actorViewModel: ActorViewModel = hiltViewModel()
                    TopMoviesScreen(
                        viewModel = actorViewModel,
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = TopSeriesScreenRoute.PATTERN,
                    arguments = listOf(navArgument(TopSeriesScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) {
                    val actorViewModel: ActorViewModel = hiltViewModel()
                    TopSeriesScreen(
                        viewModel = actorViewModel,
                        navigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = ActorGalleryScreenRoute.PATTERN,
                    arguments = listOf(navArgument(ActorGalleryScreenRoute.ARG_ACTOR_ID) {
                        type = NavType.IntType
                    })
                ) {
                    val actorViewModel: ActorViewModel = hiltViewModel()
                    ActorGalleryScreen(
                        viewModel = actorViewModel,
                        navigateBack = { navController.popBackStack() }
                    )
                }

                // ──── Reviews ──────────────────────────────────────────────────────────
                composable(
                    route = ReviewsScreenRoute.PATTERN,
                    arguments = listOf(
                        navArgument(ReviewsScreenRoute.ARG_SERIES_ID) { type = NavType.IntType },
                        navArgument(ReviewsScreenRoute.ARG_MEDIA_TYPE) { type = NavType.StringType }
                    )
                ) {
                    val reviewViewModel: ReviewViewModel = hiltViewModel()
                    ReviewsScreen(viewModel = reviewViewModel)
                }

                // ──── Movies ───────────────────────────────────────────────────────────
                composable(
                    route = MovieDetailsScreenRoute.PATTERN,
                    arguments = listOf(navArgument(MovieDetailsScreenRoute.ARG_MOVIE_ID) {
                        type = NavType.IntType
                    })
                ) {
                    val movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
                    MovieDetailsScreen(viewModel = movieDetailsViewModel)
                }

                // ──── Categories ───────────────────────────────────────────────────────
                composable(
                    route = MovieCategoriesScreenRoute.PATTERN,
                    arguments = listOf(
                        navArgument(MovieCategoriesScreenRoute.ARG_CATEGORY_ID) {
                            type = NavType.IntType
                        },
                        navArgument(MovieCategoriesScreenRoute.ARG_CATEGORY_NAME) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    val genreMoviesViewModel: GenreMoviesViewModel = hiltViewModel()
                    GenreMoviesScreen(viewModel = genreMoviesViewModel)
                }

                composable(
                    route = GenreTvShowsScreenRoute.PATTERN,
                    arguments = listOf(
                        navArgument(GenreTvShowsScreenRoute.ARG_GENRE_ID) {
                            type = NavType.IntType
                        },
                        navArgument(GenreTvShowsScreenRoute.ARG_GENRE_NAME) {
                            type = NavType.StringType
                        }
                    )
                ) {
                    val genreTvShowsViewModel: GenreTvShowsViewModel = hiltViewModel()
                    GenreTvShowsScreen(viewModel = genreTvShowsViewModel)
                }
            }
        }
    }
}