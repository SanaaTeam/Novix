package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.state.TvShowUiModel

@Composable
fun TvShowsContent(tvShows: List<TvShowUiModel>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        modifier = Modifier
            .background(color = Theme.colors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tvShows) { movie ->
            MovieSeriesPosterCard(
                poster = rememberAsyncImagePainter(model = movie.imageUrl),
                topLeftContent = {
                    SaveIconChip(
                        onClick = {}
                    )
                }
            )
        }
    }
}
