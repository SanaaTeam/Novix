package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.module.EpisodeUiModel

@Composable
fun EpisodesContent(
    episodes: List<EpisodeUiModel>,
    modifier: Modifier = Modifier,
    seriesId: Int,
    onEpisodeClick: (Int, Int, Int) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(R.string.episodes_count, episodes.size),
            style = Theme.textStyle.label.small,
            color = Theme.colors.hint,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        episodes.forEach {
            EpisodeCard(
                episode = it, modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onEpisodeClick(seriesId, it.seasonNumber, it.number)
                        })
            )
        }
    }
}