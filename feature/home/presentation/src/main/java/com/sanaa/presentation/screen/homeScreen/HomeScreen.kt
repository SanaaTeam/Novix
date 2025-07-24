package com.sanaa.presentation.screen.homeScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.components.cards.HomeTopBar
import com.sanaa.presentation.screen.homeScreen.section.MixedMediaSection
import com.sanaa.presentation.screen.homeScreen.section.PopularMediaSection
import com.sanaa.presentation.screen.homeScreen.section.WhatToWatchSection
import com.sanaa.presentation.screen.homeScreen.section.demoMediaList

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
        val scrollState = rememberScrollState()
        Column (
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PopularMediaSection()
            WhatToWatchSection(
                onMoviesClicked = {},
                onTvShowsClicked = {},
                onPeopleClicked = {}
            )
            MixedMediaSection(
                headerLabel = "Top Rating",
                mediaItems = demoMediaList
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