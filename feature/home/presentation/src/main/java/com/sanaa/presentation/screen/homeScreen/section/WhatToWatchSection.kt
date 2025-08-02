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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.section_header.SectionHeader
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.shimmerEffect.PlaceholderWithShimmerEffect

@Composable
fun WhatToWatchSection(
    modifier: Modifier = Modifier,
    onMoviesClicked: () -> Unit,
    onTvShowsClicked: () -> Unit,
    onPeopleClicked: () -> Unit,
    isLoading: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            PlaceholderWithShimmerEffect(
                width = 166.dp,
                height = 30.dp,
                cornerRadius = 8.dp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                borderColor = Color.Transparent,
            )
        } else {
            SectionHeader(title = stringResource(R.string.what_you_want_to_watch))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            WantToWatchCard(
                label = stringResource(R.string.movies),
                painter = painterResource(R.drawable.popcorns),
                brush = Theme.colors.moviesCardGradient,
                onClick = { onMoviesClicked() },
                modifier = Modifier.weight(1f)
            )
            WantToWatchCard(
                label = stringResource(R.string.tvshows),
                painter = painterResource(R.drawable.move_role),
                brush = Theme.colors.tvShowCardGradient,
                onClick = { onTvShowsClicked() },
                modifier = Modifier.weight(1f)
            )
            WantToWatchCard(
                label = stringResource(R.string.people),
                painter = painterResource(R.drawable.cenima_board),
                brush = Theme.colors.peopleCardGradient,
                onClick = { onPeopleClicked() },
                modifier = Modifier.weight(1f)
            )
        }

    }
}


@Composable
private fun WantToWatchCard(
    modifier: Modifier = Modifier,
    label: String,
    painter: Painter,
    brush: Brush,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(127.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(
                    brush = brush,
                )
                .align(Alignment.BottomCenter)
                .clickable(onClick = onClick)
        ) {
            BasicText(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                text = label,
                style = Theme.textStyle.title.medium.copy(color = Theme.colors.onPrimary),
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
            WhatToWatchSection(onMoviesClicked = {}, onTvShowsClicked = {}, onPeopleClicked = {})
        }
    }

}