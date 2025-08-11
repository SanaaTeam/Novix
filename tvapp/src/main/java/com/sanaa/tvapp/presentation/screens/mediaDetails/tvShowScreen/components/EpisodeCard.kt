package com.sanaa.tvapp.presentation.screens.mediaDetails.tvShowScreen.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import coil.compose.AsyncImage
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.DotSeparator
import com.sanaa.tvapp.presentation.screens.mediaDetails.components.IconWithText
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.EpisodeUiModel
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R

@Composable
fun EpisodeCard(
    episode: EpisodeUiModel,
    onEpisodeCardClick:()->Unit,
    modifier: Modifier = Modifier
) {

    val placeholderResId = if (isSystemInDarkTheme()) {
        designSystemResource.drawable.icon_placeholder_dark
    } else {
        designSystemResource.drawable.icon_placeholder_light
    }

    Card(
        modifier = modifier,
        onClick = {
            onEpisodeCardClick()
        },
        colors = CardDefaults.colors(Color.Transparent),
        border = CardDefaults.border(
            border = Border.None,
            focusedBorder = Border(
                border = BorderStroke(
                    width = 3.dp,
                    color = Theme.colors.primary,
                ),
                shape = RoundedCornerShape(12.dp),
            ),
        ),
        scale = CardDefaults.scale(focusedScale = 1.05f),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
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
                AppText(
                    text = stringResource(R.string.episode_number, episode.number),
                    style = Theme.textStyle.label.large,
                    color = Theme.colors.title
                )
                AppText(
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
                        AppText(
                            text = episode.airDate,
                            style = Theme.textStyle.label.small,
                            color = Theme.colors.hint
                        )
                    }
                }
            }

        }
    }

}