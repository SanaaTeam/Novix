package com.sanaa.presentation.screen.mediaTabScreen.screenContent

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.MediaListSectionContent
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

@Composable
fun MediaTabScreenContent(
    title: String,
    state: MediaTabScreenUiState,
    interactionListener: MediaTabScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        NovixTopBar(
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = interactionListener::onBackClick
                )
            },
            screenTitle = title,
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
        )

        MediaTabs(
            onTabClick = interactionListener::onMediaTabSelection,
            selectedTab = state.selectedMediaType,
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedContent(
            targetState = state.selectedMediaType,
            modifier = Modifier.padding(top= 8.dp)
        ) { selectedMediaType ->
            when (selectedMediaType) {

                MediaType.MOVIE -> {
                    MediaListSectionContent(
                        genres = state.movieGenres,
                        mediaList = state.movieList,
                        selectedGenreId = state.movieSelectedGenreId,
                        onGenreClick = interactionListener::onMovieGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaType)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                }

                MediaType.TV_SHOW -> {
                    MediaListSectionContent(
                        genres = state.tvShowGenres,
                        mediaList = state.tvShowList,
                        selectedGenreId = state.tvShowSelectedGenreId,
                        onGenreClick = interactionListener::onTvShowGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaType)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                }
            }

        }
    }
}

@PreviewLightDark
@Composable
fun MediaTabScreenContentPreview() {

    var state by remember {
        mutableStateOf<MediaTabScreenUiState>(
            MediaTabScreenUiState(
                movieGenres = listOf(
                    GenreUiState(id = 1, name = "Action"),
                    GenreUiState(id = 2, name = "Drama"),
                    GenreUiState(id = 3, name = "Crime"),
                ),
                movieList = listOf(
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
                ),
                movieSelectedGenreId = null
            )
        )
    }
    NovixTheme(isSystemInDarkTheme()) {
        MediaTabScreenContent(
            title = stringResource(R.string.top_rated),
            state = state,
            interactionListener = object : MediaTabScreenInteractionListener {
                override fun onMediaTabSelection(mediaType: MediaType) {
                    state = state.copy(selectedMediaType = mediaType)
                }

                override fun onMovieGenreClick(id: Int?) {
                    state = state.copy(movieSelectedGenreId = id)
                }

                override fun onTvShowGenreClick(id: Int?) {}

                override fun onMediaClick(id: Int, mediaType: MediaType) {}
                override fun onSaveIconClick(media: MediaItem) {}
                override fun onBackClick() {}
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colors.surface)
        )
    }
}