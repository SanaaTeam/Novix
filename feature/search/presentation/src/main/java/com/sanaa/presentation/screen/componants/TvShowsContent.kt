package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.RecentViewedUiModel
import com.sanaa.presentation.screen.state.TvShowUiModel

@Composable
fun TvShowsContent(tvShows: List<TvShowUiModel>, onTvShowClick: (RecentViewedUiModel) -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tvShows) { tvShow ->
            MovieSeriesPosterCard(boastImage = {
                RemoteBlurredHaramImageViewer(
                    imageUrl = tvShow.imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    blurRadius = 150,
                    haramThreshold = 0.2f,
                    nonHaramThreshold = 0.7f,
                    contentDescription = tvShow.title,
                    placeholder = painterResource(placeholderResId),
                    error = painterResource(placeholderResId),
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
            }, topLeftContent = {
                SaveIconChip(onClick = {})
            }, onCardClick = {
                onTvShowClick(
                    RecentViewedUiModel(
                        id = tvShow.id,
                        imageUrl = tvShow.imageUrl,
                        mediaType = MediaTypeUi.TV_SERIES.name
                    )
                )
            })
        }
    }
}

@Preview
@Composable
private fun PreviewTvShowsContent() {
    val tvShows = listOf(
        TvShowUiModel(1, "TvShow1", "https://image.com", "1"),
        TvShowUiModel(1, "TvShow2", "https://image.com", "1"),
        TvShowUiModel(1, "TvShow3", "https://image.com", "1"),
        TvShowUiModel(1, "TvShow4", "https://image.com", "1"),
    )
    NovixTheme(isSystemInDarkTheme()) {
        TvShowsContent(tvShows) {}
    }
}