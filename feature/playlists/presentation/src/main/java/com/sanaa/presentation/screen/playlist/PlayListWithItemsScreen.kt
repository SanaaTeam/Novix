package com.sanaa.presentation.screen.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.FabButton
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.feature.playlists.presentation.R
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.screen.playlist.componants.MyListItem

@Composable
fun PlayListWithItemsScreen(
    lists: List<PlayListUiModel>,
    interactionListener: PlayListItemsInteractionListener,
    isUserLoggedIn: Boolean,
    isVisible: Boolean = false,
) {
    NovixScaffold(
        topBar = {
            TopBar(
                screenTitle = stringResource(R.string.my_lists),
                modifier = Modifier
                    .statusBarsPadding()
            )
        },
        floatingActionButton = {
            FabButton(
                icon = painterResource(id = com.sanaa.designsystem.R.drawable.icon_plus),
                onClick = interactionListener::onAddNewListClicked,
                isLoading = false,
                isEnabled = true
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(lists) { list ->
                MyListItem(
                    onItemClick = {
                        interactionListener.onNavigateToSavedDetails(
                            list.id,
                            list.title
                        )
                    },
                    title = list.title,
                    count = list.mediaCount
                )
            }
        }

        if (isUserLoggedIn) {
            AddBookmarkListBottomSheet(
                isVisible = isVisible,
                onDismiss = interactionListener::onDismissAddBottomSheet,
            )
        }
    }
}
