package com.sanaa.presentation.screen.componants

import android.util.Log
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.screen.state.MediaTypeUi
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel

@Composable
fun MoviesContent(movies: List<MovieUiModel>, onMovieClick: (RecentViewedUiModel) -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme)
        R.drawable.movie_placeholder_dark
    else
        R.drawable.movie_placeholder_light

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            Log.d("MoviesContent", "Movie: $movie")
            MovieSeriesPosterCard(boastImage = {
                RemoteCensoredImageViewer(
                    imageUrl = movie.imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    blurRadius = 150,
                    sfwThreshold = 0.75f,
                    nsfwThreshold = 0.15f,
                    placeholder = painterResource(placeholderResId),
                    error = painterResource(placeholderResId),
                    contentDescription = movie.title,
                    placeholderBackgroundColor = Theme.colors.surface,
                    hintText = stringResource(R.string.unsuitable_image),
                    textStyle = Theme.textStyle.body.small,
                    iconSize = 24.dp,
                )
            }, topLeftContent = {
                SaveIconChip(onClick = {})
            }, onCardClick = {
                onMovieClick(
                    RecentViewedUiModel(
                        id = movie.id,
                        imageUrl = movie.imageUrl,
                        mediaType = MediaTypeUi.MOVIE.name
                    )
                )
            }
            )
        }
    }
}