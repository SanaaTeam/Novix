package com.sanaa.tvapp.presentation.screens.category

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.tvapp.presentation.components.TVNetworkDisconnectionContact
import com.sanaa.tvapp.presentation.screens.category.CategoriesScreenUiState.Companion.MOVIE_TAB_INDEX
import com.sanaa.tvapp.presentation.screens.category.compnents.CategoriesGrid
import com.sanaa.tvapp.presentation.screens.login.components.NovixAnimatedSnackBarHost
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.GenreMovieScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.GenreTvShowsScreenRoute
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current

    CategoryScreenEffectHandler(viewModel, navController)


    CategoriesScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
private fun CategoryScreenEffectHandler(
    viewModel: CategoriesScreenViewModel,
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CategoriesScreenEffects.NavigateToMovieGenreDetails -> {
                    navController.navigate(
                        GenreMovieScreenRoute(
                            genreId = effect.genreId,
                            genreName = effect.genreName
                        )
                    )
                }

                is CategoriesScreenEffects.NavigateToTvGenreDetails -> {
                    navController.navigate(
                        GenreTvShowsScreenRoute(
                            genreId = effect.genreId,
                            genreName = effect.genreName
                        )
                    )
                }
            }
        }
    }
}


@Composable
private fun CategoriesScreenContent(
    state: CategoriesScreenUiState,
    interactionListener: CategoriesScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = {},
        snackBarHost = {
            NovixAnimatedSnackBarHost(
                data = state.snackBarData,
                onDismiss = interactionListener::onSnackBarDismiss,
                modifier = Modifier.statusBarsPadding()
            )
        },
    ) {
        when {
            state.isNoInternet -> {
                TVNetworkDisconnectionContact(
                    onRetryClick = interactionListener::onRetryClick,
                )
            }

            else -> {
                Column {
                    Crossfade(targetState = state.isLoading) { isLoading ->
                        when {
                            isLoading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadingIndicator()
                                }
                            }

                            else -> {
                                CategoriesGrid(
                                    categories = if (state.selectedTabIndex == MOVIE_TAB_INDEX)
                                        state.movieCategories else state.tvCategories,
                                    onCategoryClick = interactionListener::onGenreClicked,
                                    interactionListener = interactionListener,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}