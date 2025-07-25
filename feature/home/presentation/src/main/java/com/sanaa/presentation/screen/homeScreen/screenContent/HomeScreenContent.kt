package com.sanaa.presentation.screen.homeScreen.screenContent

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.components.MediaListSectionContent
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.screen.homeScreen.HomeScreenInteractionListener
import com.sanaa.presentation.screen.homeScreen.HomeScreenUiState
import com.sanaa.presentation.screen.homeScreen.section.MixedMediaSection
import com.sanaa.presentation.screen.homeScreen.section.PopularMediaSection
import com.sanaa.presentation.screen.homeScreen.section.WhatToWatchSection
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenUiState,
    interactionListener: HomeScreenInteractionListener,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    NovixScaffold(
        topBar = {
            HomeTopBar(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, bottom = 16.dp)
            )
        }
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PopularMediaSection(
                mediaItems = state.popularMedia,
                onMediaClick = {
                    interactionListener.onMediaClick(it.id,it.mediaType)
                },
                onSaveIconClicked = {
                    interactionListener.onSaveIconClick(it)
                }
            )
            WhatToWatchSection(
                onMoviesClicked = {
                    interactionListener.onMoviesCardClicked()
                },
                onTvShowsClicked = {
                    interactionListener.onTvShowsCardClicked()
                },
                onPeopleClicked = {
                    interactionListener.onPeopleCardClicked()
                }
            )
            MixedMediaSection(
                headerLabel = "Top Rating",
                mediaItems = state.topRatingMedia,
                onMediaClick = {
                    interactionListener.onMediaClick(it.id,it.mediaType)
                },
                onSaveIconClicked = {
                    interactionListener.onSaveIconClick(it)
                }
            )
            MixedMediaSection(
                headerLabel = "Continue watching",
                mediaItems = state.continueWatchingMedia,
                onMediaClick = {
                    interactionListener.onMediaClick(it.id,it.mediaType)
                },
                onSaveIconClicked = {
                    interactionListener.onSaveIconClick(it)
                }
            )
            MediaListSectionContent(
                genres = state.movieGenres,
                mediaList = state.upcomingMovies,
                selectedGenreId = state.movieSelectedGenreId,
                onGenreClick = {
                    interactionListener.onMovieGenreClick(it)
                },
                onMediaClick = {
                    interactionListener.onMediaClick(it.id,it.mediaType)
                },
                onSaveIconClick = {
                    interactionListener.onSaveIconClick(it)
                },
                isScrollEnabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight.dp)
            )
        }

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
        ),
        MediaItem(
            id = 2,
            title = "Movie 2",
            imageUrl = "https://example.com/image2.jpg",
            mediaType = MediaType.MOVIE,
        ),
        MediaItem(
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
    NovixTheme (isSystemInDarkTheme()){
        HomeScreenContent(
            state = state,
            interactionListener = object : HomeScreenInteractionListener{
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
                },
        )
    }
}