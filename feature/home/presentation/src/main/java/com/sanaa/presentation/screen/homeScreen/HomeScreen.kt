package com.sanaa.presentation.screen.homeScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.screen.homeScreen.section.PopularMediaSection
import com.sanaa.presentation.screen.homeScreen.section.WhatToWatchSection

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    HomeScreenContent()
}

@Composable
fun HomeScreenContent(
) {
    NovixScaffold (
        topBar = {
            HomeTopBar(
                modifier = Modifier.padding(top = 12.dp, start = 16.dp, bottom = 16.dp)
            )
        }
    ){
        Column {
            PopularMediaSection()
            WhatToWatchSection(
                onMoviesClicked = {},
                onTvShowsClicked = {},
                onPeopleClicked = {}
            )
        }

    }
}


@PreviewLightDark()
@Composable
private fun HomeScreenPreview(modifier: Modifier = Modifier) {
    NovixTheme(
        isDarkMode = isSystemInDarkTheme()
    ) {
        HomeScreen()
    }

}