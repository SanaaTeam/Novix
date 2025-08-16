package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.EpisodeUiModel
import androidx.compose.foundation.layout.height

@Composable
fun EpisodesContent(
    episodes: List<EpisodeUiModel>,
    modifier: Modifier = Modifier,
    seriesId: Int,
    onEpisodeClick: (Int, Int, Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        AppText(
            text = stringResource(R.string.episodes_count, episodes.size),
            style = Theme.textStyle.label.small,
            color = Theme.colors.hint,
            modifier = Modifier.padding(start = 36.dp, end = 36.dp, top = 8.dp, bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
        ) {
            items(episodes) { episode ->
                EpisodeCard(
                    episode = episode,
                    onEpisodeCardClick = {
                        onEpisodeClick(seriesId, episode.seasonNumber, episode.number)
                    }
                )
            }
        }
    }
}