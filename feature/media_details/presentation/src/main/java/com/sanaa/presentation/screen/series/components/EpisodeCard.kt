package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.designsystem.R as RDesignSystem
import com.sanaa.presentation.shared_component.DotSeparator
import com.sanaa.presentation.shared_component.IconWithText
import com.sanaa.presentation.model.EpisodeUiModel

@Composable
fun EpisodeCard(
    episode: EpisodeUiModel, modifier: Modifier = Modifier
) {

    val placeholderResId = if (isSystemInDarkTheme()) {
        RDesignSystem.drawable.icon_placeholder_dark
    } else {
        RDesignSystem.drawable.icon_placeholder_dark
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .width(116.dp)
                .height(78.dp)
                .border(
                    width = 1.dp,
                    color = Theme.colors.stroke,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            episode.stillPath?.takeIf { it.isNotEmpty() }?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
                Image(
                    painter = painterResource(placeholderResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.episode_number, episode.number),
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
                episode.rating?.let {
                    IconWithText(
                        iconRes = R.drawable.icon_star,
                        text = episode.rating,
                        contentDescription = episode.rating,
                        tint = Theme.colors.statusColors.yellowAccent
                    )
                    DotSeparator()
                }
                episode.duration?.let {

                    IconWithText(
                        iconRes = R.drawable.icon_duration,
                        text = stringResource(R.string.minutes_duration, episode.duration),
                        contentDescription = episode.duration.toString(),
                        tint = Theme.colors.hint
                    )
                    DotSeparator()
                }
                episode.airDate?.let {
                    Text(
                        text = episode.airDate,
                        style = Theme.textStyle.label.small,
                        color = Theme.colors.hint
                    )
                }
            }
        }

    }
}