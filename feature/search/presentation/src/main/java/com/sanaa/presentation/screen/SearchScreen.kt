package com.sanaa.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.screen.componants.SearchSection

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    SearchScreenContent()
}

@Composable
fun SearchScreenContent() {
    NovixScaffold(
        topBar = {
            AppTopBar(
                modifier = Modifier.statusBarsPadding(),
                screenTitle = "Search"

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchSection()
        }

    }
}

@Preview(showBackground = true, locale = "en")
@Composable
private fun SearchScreenPreviewLight() {
    NovixTheme(false) {
        SearchScreenContent()
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
private fun SearchScreenPreviewDark() {
    NovixTheme(true) {
        SearchScreenContent()
    }
}