package com.sanaa.presentation.screen.genreMovies

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.api.AuthStartRoute
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.BackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.presentation.api.LocalThemeProvider
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.navigation.DetailsApiEntryPoint
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.screen.genreMovies.components.GenreMoviesGrid
import com.sanaa.presentation.screen.genreMovies.components.GenreMoviesTopBar
import com.sanaa.presentation.shared_component.RequestToLoginBottomSheet
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GenreMoviesScreen(
    viewModel: GenreMoviesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GenreMoviesEffectsHandler(effects = viewModel.effect)

    GenreMoviesScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
private fun GenreMoviesEffectsHandler(
    effects: SharedFlow<GenreMoviesEffects>,
) {
    val navController = LocalNavControllerProvider.current
    val context = LocalContext.current

    val authApi = EntryPointAccessors
        .fromApplication(
            context, DetailsApiEntryPoint::class.java
        ).authenticationApi()

    LaunchedEffect(Unit) {
        effects.collectLatest { effect ->
            when (effect) {
                GenreMoviesEffects.NavigateBack -> if (!navController.popBackStack()) {
                    (navController.context as Activity).finish()
                }

                is GenreMoviesEffects.NavigateToMovieDetails -> navController.navigate(
                    MovieDetailsScreenRoute(effect.id)
                )

                GenreMoviesEffects.NavigateToLogin -> {
                    authApi.launch(context, AuthStartRoute.Login)
                }
            }
        }
    }
}
@Composable
private fun GenreMoviesScreenContent(
    state: GenreMoviesScreenUiState,
    interactionListener: GenreMoviesScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = { BackgroundShapes() }
    ) {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            GenreMoviesTopBar(
                title = state.title.orEmpty(),
                onBackClick = interactionListener::onBackClick
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val pagedMovies = state.movies.collectAsLazyPagingItems()

                val screenState = when {
                    pagedMovies.loadState.refresh is LoadState.Loading -> ScreenState.LOADING
                    pagedMovies.loadState.refresh is LoadState.Error -> ScreenState.NO_INTERNET
                    else -> ScreenState.CONTENT
                }

                AnimatedContent(
                    targetState = screenState,
                    contentAlignment = Alignment.Center,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { target ->
                    when (target) {
                        ScreenState.NO_INTERNET -> {
                            NetworkDisconnectionContact(
                                onRetryClick = interactionListener::onRetryClicked,
                                modifier = Modifier.fillMaxSize(),
                                useDarkTheme = LocalThemeProvider.current
                            )
                        }

                        ScreenState.LOADING -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        }

                        ScreenState.CONTENT -> {
                            GenreMoviesGrid(
                                pagedMovies = pagedMovies,
                                interactionListener
                            )
                        }
                    }
                }

                state.selectedMovieToSave?.let { mediaItem ->
                    SaveToListBottomSheet(
                        isVisible = state.showSaveToListBottomSheet,
                        mediaId = state.selectedMovieToSave?.id ?: 0,
                        onDismiss = interactionListener::onDismissSaveToListBottomSheet,
                        onCreateNewListClick = interactionListener::onCreateNewListClick,
                    )
                }

    AddBookmarkListBottomSheet(
        isVisible = state.showAddListBottomSheet,
        onDismiss = interactionListener::onDismissAddListBottomSheet,
    )
    RequestToLoginBottomSheet(
        onDismiss = { interactionListener.onBottomSheetDismiss() },
        onLoginButtonClick = { interactionListener.onLoginButtonClick() },
        isVisible = state.showBottomSheet
    )
}