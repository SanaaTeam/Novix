package com.sanaa.tvapp.presentation.screens.category

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.tvapp.presentation.screens.category.compnents.CategoriesGrid
import com.sanaa.tvapp.presentation.screens.category.compnents.CategoryTopBar
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.GenreMovieScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.GenreTvShowsScreenRoute

@Composable
fun CategoriesScreen(
    viewModel: CategoriesScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
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


    CategoriesScreen(
        state = state,
        interactionListener = viewModel
    )
}


@Composable
private fun CategoriesScreen(
    state: CategoriesScreenUiState,
    interactionListener: CategoriesScreenInteractionListener,
) {
    NovixScaffold(
        topBar = {
            CategoryTopBar(
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = interactionListener::onTabChanged
            )
        }
    ) {
        when {
            state.isNoInternet -> {
                NetworkDisconnectionContact(
                    onRetryClick = interactionListener::onRetryClick,
                )
            }

            else -> {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {

                    Crossfade(
                        targetState = state.isLoading,

                        ) { isLoading ->
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }

                        } else {
                            CategoriesGrid(
                                categories = if (state.selectedTabIndex ==
                                    CategoriesScreenUiState.MOVIE_TAB_INDEX
                                )
                                    state.movieCategories
                                else
                                    state.tvCategories,
                                onCategoryClick = interactionListener::onGenreClicked
                            )
                        }
                    }
                }
            }
        }
    }
}