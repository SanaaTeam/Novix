package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.component.DotSeparator
import com.sanaa.presentation.component.IconWithText
import com.sanaa.presentation.screen.series.EpisodeUiModel

@Composable
fun EpisodeCard(
    episode: EpisodeUiModel, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = episode.stillPath, contentDescription = null, modifier = Modifier
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .fillMaxHeight()
                .width(116.dp)
                .height(78.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.episode_number, episode.episodeNumber),
                style = Theme.textStyle.label.large,
                color = Theme.colors.title
            )
            Text(
                text = episode.title,
                style = Theme.textStyle.label.small,
                color = Theme.colors.hint
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconWithText(
                    iconRes = R.drawable.icon_star,
                    text = episode.rating,
                    contentDescription = episode.rating,
                    tint = Theme.colors.statusColors.yellowAccent
                )
                DotSeparator()
                IconWithText(
                    iconRes = R.drawable.icon_duration,
                    text = stringResource(R.string.m, episode.duration),
                    contentDescription = episode.duration.toString(),
                    tint = Theme.colors.hint
                )
                DotSeparator()
                Text(
                    text = episode.airDate,
                    style = Theme.textStyle.label.small,
                    color = Theme.colors.hint
                )
            }
        }

    }
}