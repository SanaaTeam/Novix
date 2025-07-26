package com.sanaa.presentation.screen.trendingMediaScreen.screenContent

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState
import com.sanaa.presentation.state.GenreUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

@Composable
fun TrendingMediaScreenContent(
    title: String,
    state: TrendingMediaScreenUiState,
    interactionListener: MediaListScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                .statusBarsPadding()
        )

        MediaListSectionContent(
            genres = state.genreList,
            mediaList = state.mediaList,
            selectedGenreId = state.selectedGenreId,
            onGenreClick = interactionListener::onGenreClick,
            onMediaClick = { media -> interactionListener.onMediaClick(media.id) },
            onSaveIconClick = interactionListener::onSaveIconClick,
        )
    }
}

@PreviewLightDark
@Composable
fun TrendingMediaScreenContentPreview() {

    var state by remember {
        mutableStateOf<TrendingMediaScreenUiState>(
            TrendingMediaScreenUiState(
                genreList = listOf(
                    GenreUiState(id = 1, name = "Action"),
                    GenreUiState(id = 2, name = "Drama"),
                    GenreUiState(id = 3, name = "Crime"),
                ),
                mediaList = listOf(
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
                selectedGenreId = null
            )
        )
    }
    NovixTheme(isSystemInDarkTheme()) {
        TrendingMediaScreenContent(
            title = stringResource(R.string.trending_movies),
            state = state,
            interactionListener = object : MediaListScreenInteractionListener {
                override fun onGenreClick(id: Int?) {
                    state = state.copy(selectedGenreId = id)
                }

                override fun onMediaClick(id: Int) {}
                override fun onSaveIconClick(media: MediaItem) {}
                override fun onBackClick() {}
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.colors.surface)
        )
    }
}