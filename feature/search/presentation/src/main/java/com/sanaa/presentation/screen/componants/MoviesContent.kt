package com.sanaa.presentation.screen.componants

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.state.MovieUiModel

@Composable
fun MoviesContent(movies: List<MovieUiModel>) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                        nsfwThreshold = 0.5f
                    )
                },
                topLeftContent = {
                    SaveIconChip(onClick = {})
                }

            )
        }
    }

}