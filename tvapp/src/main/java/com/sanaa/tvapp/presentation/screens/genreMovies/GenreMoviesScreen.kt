package com.sanaa.tvapp.presentation.screens.genreMovies

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.loading.LoadingIndicator
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.category.util.getGenreImage
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MovieDetailsRoute
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard

@Composable
fun GenreMoviesScreen(
    viewModel: GenreMoviesViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                GenreMoviesEffects.NavigateBack -> if (!navController.popBackStack()) {
                    (navController.context as Activity).finish()
                }

                is GenreMoviesEffects.NavigateToMovieDetails ->
                    navController.navigate(MovieDetailsRoute(effect.id))


                GenreMoviesEffects.NavigateToLogin -> {

                }

            }
        }
    }

    GenreMoviesScreenContent(
        state = state.value, interactionListener = viewModel
    )


}


@Composable
fun GenreMoviesScreenContent(
    state: GenreMoviesScreenUiState,
    interactionListener: GenreMoviesScreenInteractionListener,
) {
    val pagedMovies = state.movies.collectAsLazyPagingItems()
    NovixScaffold(

        backgroundShapes = {
            Image(
                painter = painterResource(id = getGenreImage(state.genreid)),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        },

        ) {
        Column {

            GenreMoviesTopBar(state.title.toString())
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = state.isLoading || state.noInternetConnection,
                    contentAlignment = Alignment.Center,
                    transitionSpec = { fadeIn() togetherWith fadeOut() })
                {
                    if (it) {
                        if (state.noInternetConnection) {
                            NetworkDisconnectionContact(
                                onRetryClick = { interactionListener.onRetryClicked() },
                                modifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 64.dp),

                            columns = GridCells.Fixed(5),
                            contentPadding = PaddingValues(
                                16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                count = pagedMovies.itemCount,
                                key = { index ->
                                    "${index}_${pagedMovies[index]?.id}"
                                }
                            ) { index ->
                                val movie = pagedMovies[index] ?: return@items

                                val interactionSource = remember { MutableInteractionSource() }
                                val isFocused by interactionSource.collectIsFocusedAsState()
                                val scale by animateFloatAsState(
                                    targetValue = if (isFocused) 1.1f else 1f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                                Surface(
                                    modifier = Modifier
                                        .focusable(interactionSource = interactionSource)
                                        .graphicsLayer(
                                            scaleX = scale,
                                            scaleY = scale
                                        )
                                        .border(
                                            width = if (isFocused) 3.dp else 1.dp,
                                            color = if (isFocused) Theme.colors.primary else Theme.colors.stroke,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null,

                                            onClick = {
                                                interactionListener.onMovieClick(movie.id)
                                            }
                                        )
                                ) {
                                    TvMediaPosterCard(
                                        title = movie.title,
                                        imageUrl = movie.imageUrl.orEmpty(),
                                        onCardClick = {
                                        },
                                    )

                                }
                            }

                            if (pagedMovies.loadState.append is LoadState.Loading) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LoadingIndicator()
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun GenreMoviesTopBar(
    genreName: String,
    modifier: Modifier = Modifier,


    ) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Movies\\ $genreName",

            style = Theme.textStyle.title.medium,
            color = Theme.colors.body
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_logo),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Theme.colors.primary)
            )
            Text(
                text = "Novix",
                style = Theme.textStyle.title.medium,
                color = Theme.colors.body
            )
        }
    }
}