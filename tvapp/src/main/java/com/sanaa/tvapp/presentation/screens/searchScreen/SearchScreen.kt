package com.sanaa.tvapp.presentation.screens.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.RemoteImagePlaceholder
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.SearchTextField
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvTopBar


@Composable
fun SearchScreen(
    searchViewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = uiState.movies.collectAsLazyPagingItems()
    val tvShowsPagingData = uiState.tvShows.collectAsLazyPagingItems()
    val actorsPagingData = uiState.actors.collectAsLazyPagingItems()
    NovixTheme(isSystemInDarkTheme()) {
        SearchScreenContent(
            uiState = uiState,
            searchListener = searchViewModel,
            moviesPagingData = moviesPagingData,
            tvShowsPagingData = tvShowsPagingData,
            actorsPagingData = actorsPagingData,
        )
    }
}

@Composable
fun SearchScreenContent(
    uiState: SearchTvScreenUiState,
    searchListener: SearchScreenInteractionListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {

    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TvTopBar(
            selectedTabIndex = uiState.selectedTabIndex,
            onTabSelected = searchListener::onTabSelected
        )

        SearchTextField(
            text = text,
            onTextChange = {
                text = it
                searchListener.onSearchQueryChanged(it)
            },
            modifier = Modifier
        )

        when (uiState.selectedTabIndex) {
            SearchTvScreenUiState.MOVIE_INDEX -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    items(moviesPagingData.itemCount) { index ->
                        val movie = moviesPagingData[index]
                        if (movie != null) {
                            TvMediaPosterCard(
                                title = movie.title,
                                posterImage = {
                                    RemoteBlurredSensitiveImage(
                                        imageUrl = movie.imageUrl,
                                        modifier = Modifier.fillMaxWidth(),
                                        sensitiveContentThreshold = 0.2f,
                                        safeContentThreshold = 0.7f,
                                        placeholderContent = {
                                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                                        },
                                        errorContent = {
                                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                                        },
                                        contentDescription = movie.title,
                                    ) {
                                        OnBlurContent(
                                            hintText = stringResource(R.string.unsuitable_image),
                                            textStyle = Theme.textStyle.body.small.copy(
                                                color = Color(0x99FFFFFF)
                                            ),
                                            iconSize = 24.dp,
                                            icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }

            SearchTvScreenUiState.TV_SHOW_INDEX -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    items(tvShowsPagingData.itemCount) { index ->
                        val show = tvShowsPagingData[index]
                        if (show != null) {
                            TvMediaPosterCard(
                                title = show.title,
                                posterImage = {
                                    RemoteBlurredSensitiveImage(
                                        imageUrl = show.imageUrl,
                                        modifier = Modifier.fillMaxWidth(),
                                        sensitiveContentThreshold = 0.2f,
                                        safeContentThreshold = 0.7f,
                                        placeholderContent = {
                                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                                        },
                                        errorContent = {
                                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                                        },
                                        contentDescription = show.title,
                                    ) {
                                        OnBlurContent(
                                            hintText = stringResource(R.string.unsuitable_image),
                                            textStyle = Theme.textStyle.body.small.copy(
                                                color = Color(0x99FFFFFF)
                                            ),
                                            iconSize = 24.dp,
                                            icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }

            SearchTvScreenUiState.ACTOR_INDEX -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
                ) {
                    items(actorsPagingData.itemCount) { index ->
                        val actor = actorsPagingData[index]
                        if (actor != null) {
                            TvMediaPosterCard(
                                title = actor.name,
                                posterImage = {
                                    RemoteBlurredSensitiveImage(
                                        imageUrl = actor.imageUrl,
                                        modifier = Modifier.fillMaxWidth(),
                                        sensitiveContentThreshold = 0.2f,
                                        safeContentThreshold = 0.7f,
                                        placeholderContent = {
                                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                                        },
                                        errorContent = {
                                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                                        },
                                        contentDescription = actor.name,
                                    ) {
                                        OnBlurContent(
                                            hintText = stringResource(R.string.unsuitable_image),
                                            textStyle = Theme.textStyle.body.small.copy(
                                                color = Color(0x99FFFFFF)
                                            ),
                                            iconSize = 24.dp,
                                            icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
