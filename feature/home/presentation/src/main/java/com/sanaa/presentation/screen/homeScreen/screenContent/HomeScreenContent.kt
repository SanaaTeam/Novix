package com.sanaa.presentation.screen.homeScreen.screenContent

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.components.shimmerEffect.MediaSliderSectionPlaceholder
import com.sanaa.presentation.components.shimmerEffect.PopularMediaSectionPlaceholder
import com.sanaa.presentation.components.shimmerEffect.upcomingSectionPlaceholder
import com.sanaa.presentation.modifiers.fillWidthOfParent
import com.sanaa.presentation.screen.homeScreen.HomeScreenInteractionListener
import com.sanaa.presentation.screen.homeScreen.HomeScreenUiState
import com.sanaa.presentation.screen.homeScreen.section.MixedMediaSection
import com.sanaa.presentation.screen.homeScreen.section.PopularMediaSection
import com.sanaa.presentation.screen.homeScreen.section.WhatToWatchSection
import com.sanaa.presentation.screen.homeScreen.section.upcomingSection
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    interactionListener: HomeScreenInteractionListener,
) {
    NovixScaffold(
        backgroundShapes = {},
        topBar = {
            HomeTopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.isLoading)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    PopularMediaSectionPlaceholder(
                        modifier = Modifier.fillWidthOfParent(16.dp)
                    )
                }
            else
                item(span = { GridItemSpan(maxLineSpan) }) {
                    PopularMediaSection(
                        mediaItems = state.popularMedia, onMediaClick = {
                            interactionListener.onMediaClick(it.id, it.mediaType)
                        }, onSaveIconClicked = {
                            interactionListener.onSaveIconClick(it)
                        }, modifier = Modifier.fillWidthOfParent(16.dp)
                    )
                }
            item(span = { GridItemSpan(maxLineSpan) }) {
                WhatToWatchSection(
                    onMoviesClicked = {
                        interactionListener.onMoviesCardClicked()
                    }, onTvShowsClicked = {
                        interactionListener.onTvShowsCardClicked()
                    }, onPeopleClicked = {
                        interactionListener.onPeopleCardClicked()
                    }, modifier = Modifier
                        .fillWidthOfParent(16.dp)
                        .padding(top = 8.dp),
                    isLoading = state.isLoading
                )
            }
            if (state.isLoading)
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MediaSliderSectionPlaceholder(
                        modifier = Modifier
                            .fillWidthOfParent(16.dp)
                            .padding(
                                top = 24.dp
                            ),
                    )
                }
            else {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MixedMediaSection(
                        headerLabel = stringResource(R.string.top_rated),
                        modifier = Modifier
                            .fillWidthOfParent(16.dp)
                            .padding(
                                top = 24.dp
                            ),
                        mediaItems = state.topRatingMedia,
                        onMediaClick = {
                            interactionListener.onMediaClick(it.id, it.mediaType)
                        },
                        onSaveIconClicked = {
                            interactionListener.onSaveIconClick(it)
                        },
                        onViewAllClick = { interactionListener.onShowAllTopRatingClicked() })
                }
            }
            if (state.isLoading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MediaSliderSectionPlaceholder(
                        modifier = Modifier
                            .fillWidthOfParent(16.dp)
                            .padding(
                                top = 24.dp, bottom = 24.dp
                            ),
                    )
                }
            } else {
                if (state.continueWatchingMedia.isNotEmpty()) item(span = { GridItemSpan(maxLineSpan) }) {
                    MixedMediaSection(
                        headerLabel = stringResource(R.string.continue_watching),
                        mediaItems = state.continueWatchingMedia,
                        onMediaClick = {
                            interactionListener.onMediaClick(it.id, it.mediaType)
                        },
                        onSaveIconClicked = {
                            interactionListener.onSaveIconClick(it)
                        },
                        modifier = Modifier.fillWidthOfParent(16.dp),
                    )
                }
            }
            if (state.isLoading) {
                upcomingSectionPlaceholder()
            } else {
                upcomingSection(
                    upcomingMovies = state.upcomingMovies,
                    movieGenres = state.movieGenres,
                    movieSelectedGenreId = state.movieSelectedGenreId,
                    onGenreClick = interactionListener::onMovieGenreClick,
                    onSaveIconClick = interactionListener::onSaveIconClick
                )
            }
        }

        RequestToLoginBottomSheet(
            isVisible = state.showBottomSheet,
            onDismiss = interactionListener::onDismissBottomSheet
        )
    }
}


@PreviewLightDark
@Composable
fun HomeScreenContentPreview(modifier: Modifier = Modifier) {
    val mediaList = listOf(
        MediaItem(
            id = 1,
            title = "Movie 1",
            imageUrl = "https://example.com/image1.jpg",
            mediaType = MediaType.MOVIE,
        ), MediaItem(
            id = 2,
            title = "Movie 2",
            imageUrl = "https://example.com/image2.jpg",
            mediaType = MediaType.MOVIE,
        ), MediaItem(
            id = 3,
            title = "Movie 3",
            imageUrl = "https://example.com/image3.jpg",
            mediaType = MediaType.MOVIE,
        )
    )
    var state by remember {
        mutableStateOf(
            HomeScreenUiState(
                popularMedia = mediaList,
                topRatingMedia = mediaList,
                continueWatchingMedia = mediaList,
                upcomingMovies = mediaList,
                movieGenres = listOf(
                    GenreUiState(id = 1, name = "Action"),
                    GenreUiState(id = 2, name = "Drama"),
                    GenreUiState(id = 3, name = "Crime"),
                ),
                movieSelectedGenreId = null,
            )
        )
    }
    NovixTheme(isSystemInDarkTheme()) {
        HomeScreenContent(
            state = state,
            interactionListener = object : HomeScreenInteractionListener {
                override fun onMoviesCardClicked() {}
                override fun onTvShowsCardClicked() {}
                override fun onPeopleCardClicked() {}
                override fun onShowAllTopRatingClicked() {}
                override fun onShowAllContinueWatchingClicked() {}
                override fun onMovieGenreClick(id: Int?) {
                    state = state.copy(movieSelectedGenreId = id)
                }

                override fun onMediaClick(id: Int, mediaType: MediaType) {}
                override fun onSaveIconClick(media: MediaItem) {}
                override fun onDismissBottomSheet() {}
            },
        )
    }
}