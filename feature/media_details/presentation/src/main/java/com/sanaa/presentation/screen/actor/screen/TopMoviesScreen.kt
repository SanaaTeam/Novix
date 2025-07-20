package com.sanaa.presentation.screen.actor.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.loading.NovixLoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixBackgroundShapes
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.component.RemoteImagePlaceholder
import com.sanaa.presentation.component.cards.MediaPosterCard
import com.sanaa.presentation.component.cards.SaveIconChip
import com.sanaa.presentation.navigation.LocalNavControllerProvider
import com.sanaa.presentation.navigation.MovieDetailsScreenRoute
import com.sanaa.presentation.screen.actor.ActorScreenUiState
import com.sanaa.presentation.screen.actor.ActorViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TopMoviesScreen(
    actorId: Int,
    navigateBack: () -> Unit,
    viewModel: ActorViewModel = koinViewModel { parametersOf(actorId) },
) {
    BackHandler(onBack = navigateBack)

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        TopMoviesContent(
            state = uiState, onBackClick = navigateBack, modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun TopMoviesContent(
    state: ActorScreenUiState, modifier: Modifier = Modifier, onBackClick: () -> Unit,
) {
    val navController = LocalNavControllerProvider.current

    NovixScaffold(
        backgroundShapes = { NovixBackgroundShapes() },
    ) {
        Column(
            modifier = modifier.navigationBarsPadding()
        ) {
            NovixTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back), onClick = onBackClick
                    )
                },
                screenTitle = stringResource(R.string.top_movie_picks),
                modifier = Modifier
                    .fillMaxWidth()
                    .systemBarsPadding()
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    state.isLoading,
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center

                ) { loading ->
                    if (loading) {
                        NovixLoadingIndicator()
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(minSize = 140.dp),
                            contentPadding = PaddingValues(
                                start = 16.dp, end = 16.dp, bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(
                                12.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(
                                12.dp
                            )
                        ) {
                            items(
                                state.topMovies,
                                key = { item -> item.id }
                            ) { movie ->
                                MediaPosterCard(
                                    boastImage = {
                                        RemoteBlurredHaramImageViewer(
                                            imageUrl = movie.posterUrl.orEmpty(),
                                            modifier = Modifier.fillMaxSize(),
                                            blurRadius = 150,
                                            haramThreshold = 0.2f,
                                            nonHaramThreshold = 0.7f,
                                            contentDescription = movie.title,
                                            placeholderContent = {
                                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                                            },
                                            errorContent = {
                                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                                            },
                                        ) {
                                            OnBlurContent(
                                                hintText = stringResource(R.string.unsuitable_image),
                                                textStyle = Theme.textStyle.body.small.copy(
                                                    color = Color(0x99FFFFFF)
                                                ),
                                                iconSize = 24.dp,
                                                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash)
                                            )
                                        }
                                    },
                                    topLeftContent = { SaveIconChip(onClick = { /* save */ }) },
                                    onCardClick = {
                                        navController.navigate(MovieDetailsScreenRoute(movie.id).route())
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}
