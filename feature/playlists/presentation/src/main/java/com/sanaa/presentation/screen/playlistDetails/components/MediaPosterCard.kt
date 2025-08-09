package com.sanaa.presentation.screen.playlistDetails.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.playlistDetails.components.chips.MediaRatingChip
import com.sanaa.presentation.screen.playlistDetails.components.chips.SaveIconChip

@Composable
fun MediaPosterCard(
    modifier: Modifier = Modifier,
    width: Dp = 158.dp,
    height: Dp = 210.dp,
    ratio: Float = (width / height),
    onCardClick: () -> Unit = {},
    topRightContent: @Composable () -> Unit = {},
    topLeftContent: @Composable () -> Unit = {},
    posterImage: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .aspectRatio(
                ratio = ratio
            )
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.surface)
            .clickable(onClick = onCardClick)
    ) {
        posterImage()
        Box(
            modifier = Modifier
                .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
                .padding(8.dp)
                .matchParentSize()
                .align(Alignment.TopStart),
        ) {
            topLeftContent()
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                topRightContent()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewMovieSeriesPosterCard() {
    NovixTheme(isSystemInDarkTheme()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(
                minSize = 140.dp
            ),
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MediaPosterCard(
                    modifier = Modifier,
                    posterImage = {
                        Image(
                            painter = painterResource(R.drawable.icon_placeholder_light),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    },
                    topLeftContent = {
                        MediaRatingChip(
                            rating = "9.9"
                        )
                    },
                )
            }
            item {
                MediaPosterCard(
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {}
                        )
                    },
                )
            }
            item {
                MediaPosterCard(
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {}
                        )
                    },
                    topRightContent = {
                        MediaRatingChip(
                            rating = "9.9"
                        )
                    }
                )
            }
        }
    }
}