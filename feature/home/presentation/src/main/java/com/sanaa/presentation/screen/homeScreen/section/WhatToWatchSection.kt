package com.sanaa.presentation.screen.homeScreen.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.screen.homeScreen.AppGradients.moviesCardGradientColor
import com.sanaa.presentation.screen.homeScreen.AppGradients.peopleCardGradientColor
import com.sanaa.presentation.screen.homeScreen.AppGradients.tvShowCardGradientColor

@Composable
fun WhatToWatchSection(
    modifier: Modifier = Modifier,
    onMoviesClicked: () -> Unit,
    onTvShowsClicked: () -> Unit,
    onPeopleClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixSectionHeader(title = "What you want to watch?")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            WantToWatchCard(
                label = "Movies",
                painter = painterResource(R.drawable.popcorns),
                brush = moviesCardGradientColor,
                onClick = { onMoviesClicked() },
            )
            WantToWatchCard(
                label = "Movies",
                painter = painterResource(R.drawable.move_role),
                brush = tvShowCardGradientColor,
                onClick = { onTvShowsClicked() },
            )
            WantToWatchCard(
                label = "Movies",
                painter = painterResource(R.drawable.cenima_board),
                brush = peopleCardGradientColor,
                onClick = { onPeopleClicked() },
            )
        }

    }
}


@Composable
fun WantToWatchCard(
    modifier: Modifier = Modifier,
    label: String,
    painter: Painter,
    brush: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(127.dp)
            .width(104.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
                .background(
                    brush = brush,
                    shape = RoundedCornerShape(12.dp)
                )
                .align(Alignment.BottomCenter)
                .clickable {
                    onClick()
                },
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                text = label,
                style = Theme.textStyle.title.medium,
                color = Theme.colors.onPrimary
            )
        }
        Image(
            modifier = Modifier
                .height(64.dp)
                .align(Alignment.TopStart)
                .padding(start = 4.dp),
            painter = painter,
            contentDescription = null
        )
    }
}


@PreviewLightDark
@Composable
private fun WhatToWatchSectionPreview(modifier: Modifier = Modifier) {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        Column(
            modifier.fillMaxSize()
        ) {
            WhatToWatchSection(
                onMoviesClicked = {},
                onTvShowsClicked = {},
                onPeopleClicked = {}
            )
        }
    }

}