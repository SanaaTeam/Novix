package com.sanaa.designsystem.design_system.component.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.chips.MovieSeriesRatingChip
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun MovieSeriesPosterCard(
    poster: Painter,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    topRightContent: @Composable () -> Unit = {},
    topLeftContent: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .height(210.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Theme.colors.surface)
            .clickable(onClick = onCardClick)
    ) {
        Image(
            painter = poster,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .border(1.dp, Theme.colors.stroke, RoundedCornerShape(12.dp))
                .padding(8.dp)
                .matchParentSize()
                .align(Alignment.TopStart),
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                topLeftContent()
            }
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                topRightContent()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMovieSeriesPosterCard() {
    NovixTheme(false) {
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
                MovieSeriesPosterCard(
                    poster = painterResource(R.drawable.movie_poster),
                    topLeftContent = {
                        MovieSeriesRatingChip(
                            rating = "9.9"
                        )
                    },
                )
            }
            item {
                MovieSeriesPosterCard(
                    poster = painterResource(R.drawable.movie_poster),
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {}
                        )
                    },
                )
            }
            item {
                MovieSeriesPosterCard(
                    poster = painterResource(R.drawable.movie_poster),
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {}
                        )
                    },
                    topRightContent = {
                        MovieSeriesRatingChip(
                            rating = "9.9"
                        )
                    }
                )
            }
        }
    }
}