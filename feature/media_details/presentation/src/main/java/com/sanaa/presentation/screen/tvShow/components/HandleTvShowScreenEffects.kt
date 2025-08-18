package com.sanaa.presentation.screen.tvShow.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.sanaa.api.AuthenticationApi
import com.sanaa.presentation.model.MediaTypeUiModel
import com.sanaa.presentation.navigation.ActorScreenRoute
import com.sanaa.presentation.navigation.EpisodeDetailsScreenRoute
import com.sanaa.presentation.navigation.GenreTvShowsScreenRoute
import com.sanaa.presentation.navigation.ReviewsScreenRoute
import com.sanaa.presentation.screen.movieDetails.SnackData
import com.sanaa.presentation.screen.tvShow.TvShowScreenEffects
import com.sanaa.presentation.screen.tvShow.TvShowScreenViewModel


@Composable
fun HandleTvShowScreenEffects(
    viewModel: TvShowScreenViewModel,
    navController: NavController,
    context: Context,
    launcher: ActivityResultLauncher<Intent>,
    authApi: AuthenticationApi,
    onShowSnackBar: (SnackData) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TvShowScreenEffects.NavigateToActorScreen -> {
                    navController.navigate(ActorScreenRoute(effect.actorId).copy())
                }

                is TvShowScreenEffects.NavigateToEpisodeDetailsScreen -> {
                    navController.navigate(
                        EpisodeDetailsScreenRoute(
                            effect.tvShowId, effect.seasonNumber, effect.episodeNumber
                        ).copy()
                    )
                }

                is TvShowScreenEffects.NavigateToReviewsScreen -> {
                    navController.navigate(
                        ReviewsScreenRoute(effect.tvShowId, MediaTypeUiModel.TV_SHOW).copy()
                    )
                }

                is TvShowScreenEffects.NavigateBack -> {
                    if (!navController.popBackStack()) {
                        (navController.context as Activity).finish()
                    }
                }

                is TvShowScreenEffects.PlayTrailer -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.trailerUrl?.toUri())
                    context.startActivity(intent)
                }

                is TvShowScreenEffects.NavigateToMovieCategoriesScreen -> {
                    navController.navigate(GenreTvShowsScreenRoute(effect.category.id, effect.category.name).copy()
                    )
                }

                TvShowScreenEffects.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }
}