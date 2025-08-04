package com.sanaa.presentation.screen.saved

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.screen.saved.componants.MyListItem

@Composable
fun PlayListWithItemsScreen(
    lists: List<PlayListUiModel>,
    onItemClick: () -> Unit
) {
    NovixScaffold(
        topBar = {
            TopBar(
                screenTitle = stringResource(R.string.my_lists),
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(lists) { list ->
                    MyListItem(
                        onItemClick = { onItemClick() },
                        title = list.title,
                        count = list.mediaCount
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PlayListWithItemsScreenPrev() {
    NovixTheme(
        isSystemInDarkTheme()
    ) {
        PlayListWithItemsScreen(
            lists = listOf(
                PlayListUiModel(
                    id = 1,
                    title = "My List",
                    mediaCount = 10
                ),
                PlayListUiModel(
                    id = 2,
                    title = "My List",
                    mediaCount = 10
                ),
                PlayListUiModel(
                    id = 3,
                    title = "My List",
                    mediaCount = 10
                ),
            ), onItemClick = {}
        )
    }
}
