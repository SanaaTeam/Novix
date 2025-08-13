package com.sanaa.presentation.screen.trendingMediaScreen.screenContent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.bottomsheet.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheet.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.components.RefreshButton
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState

@Composable
fun TrendingMediaScreenContent(
    title: String,
    state: TrendingMediaScreenUiState,
    interactionListener: MediaListScreenInteractionListener,
) {

    val trendingMedia = state.mediaList.collectAsLazyPagingItems()

    NovixScaffold(
        topBar = {
            TopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_back),
                        onClick = interactionListener::onBackClick
                    )
                },
                screenTitle = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
        },
        modifier = Modifier.systemBarsPadding(),
    ) {

        AnimatedContent(
            targetState = state.isNoInternetConnection && trendingMedia.itemCount == 0,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) { showNoInternetScreen ->
            if (showNoInternetScreen) {
                NetworkDisconnectionContact(onRetryClick = interactionListener::onRetryClick)
            } else {
                PaginatedMediaListSectionContent(
                    genres = state.genreList,
                    mediaList = trendingMedia,
                    selectedGenreId = state.selectedGenreId,
                    onGenreClick = interactionListener::onGenreClick,
                    onMediaClick = { media -> interactionListener.onMediaClick(media.id) },
                    onSaveIconClick = interactionListener::onSaveIconClick,
                )
                if (trendingMedia.loadState.hasError) {
                    RefreshButton(onRetryClick = interactionListener::onRetryClick)
                }

                if (state.userIsLoggedIn) {
                    state.selectedMediaId?.let { mediaItem ->
                        SaveToListBottomSheet(
                            isVisible = state.showSaveToListBottomSheet,
                            mediaId = mediaItem.toLong(),
                            onDismiss = interactionListener::onDismissSaveToListBottomSheet,
                            onCreateNewListClick = interactionListener::onCreateNewListClick,
                        )
                    }
                    AddBookmarkListBottomSheet(
                        isVisible = state.showAddListBottomSheet,
                        onDismiss = interactionListener::onDismissAddListBottomSheet,
                        mediaId = state.selectedMediaId ?: 0
                    )
                } else {
                    RequestToLoginBottomSheet(
                        isVisible = state.showLoginBottomSheet,
                        onDismiss = interactionListener::onDismissLoginBottomSheet,
                        onLoginButtonClick = interactionListener::onLoginButtonClick
                    )
                }
            }
        }
    }
}
