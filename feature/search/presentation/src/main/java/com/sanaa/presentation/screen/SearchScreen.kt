package com.sanaa.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.presentation.screen.componants.SearchSection

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {

}

@Composable
fun SearchScreenContent() {
    NovixScaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            AppTopBar(
                screenTitle = "Search"
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
        ) {
            SearchSection()
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreenContent()
}