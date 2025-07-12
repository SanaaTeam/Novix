package com.sanaa.presentation.screen.componants

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import com.sanaa.designsystem.design_system.theme.Theme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.R
import com.sanaa.presentation.state.MovieUiModel

@Composable
fun MoviesContent(movies: List<MovieUiModel>) {
    val isDarkTheme = isSystemInDarkTheme()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        val placeholderResId = if (isDarkTheme) {
            R.drawable.movie_placeholder_dark
        } else {
            R.drawable.movie_placeholder_light
        }
        items(movies) { movie ->
            Log.d("MoviesContent", "Movie: $movie")
            MovieSeriesPosterCard(
                boastImage = {
                    RemoteCensoredImageViewer(
                        imageUrl = movie.imageUrl,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        blurRadius = 1000,
                        sfwThreshold = 0.7f,
                        nsfwThreshold = 0.2f,
                        placeholder = painterResource(placeholderResId),
                        error = painterResource(placeholderResId),
                    )
                },
                topLeftContent = {
                    SaveIconChip(onClick = {})
                }

            )
        }
    }

}