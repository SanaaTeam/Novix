package com.sanaa.presentation.screen.movie_categories

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.component.RequestToLoginBottomSheet
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import entity.Genre
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun MovieCategoriesScreen(
    categoryId: Genre,
    viewModel: MovieCategoriesViewModel = koinViewModel(parameters = { parametersOf(categoryId) })
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MovieCategoriesScreenEffects.NavigateBack -> navController.popBackStack()
                is MovieCategoriesScreenEffects.NavigateToMovieDetails -> navController.navigate(
                    MovieDetailsScreenRoute(effect.id).route()
                )
            }
        }
    }

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        MovieCategoriesScreenContent(
            state = state.value, interactionListener = viewModel
        )
    }

}

@Composable
fun MovieCategoriesScreenContent(
    state: MovieCategoriesScreenUiState,
    interactionListener: MovieCategoriesScreenInteractionListener
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }

    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Column(
            modifier = Modifier
        ) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = { interactionListener.onBackClick() })
                }, screenTitle = state.title?.toLocalizedString().orEmpty(), modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = state.isLoading,
                    contentAlignment = Alignment.Center,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }) { loading ->
                    if (loading) {
                        NovixLoadingIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.movies) { movie ->
                                MovieSeriesPosterCard(
                                    boastImage = {
                                        RemoteBlurredHaramImageViewer(
                                            imageUrl = movie.posterUrl.orEmpty(),
                                            modifier = Modifier.fillMaxSize(),
                                            blurRadius = 150,
                                            haramThreshold = 0.2f,
                                            nonHaramThreshold = 0.7f,
                                            contentDescription = movie.title,
                                            placeholder = painterResource(placeholderResId),
                                            error = painterResource(placeholderResId)
                                        )
                                    },
                                    topLeftContent = { SaveIconChip(onClick = { interactionListener.onSaveIconClick() }) },
                                    onCardClick = { interactionListener.onMovieClick(movie.id) })
                            }
                        }
                    }
                    if (state.showBottomSheet) {
                        RequestToLoginBottomSheet(
                            onDismiss = { interactionListener.onBottomSheetDismiss() },
                            onLoginButtonClick = {/* navigate to login screen */ })
                    }
                }
            }
        }
    }
}


