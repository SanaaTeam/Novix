package com.sanaa.presentation.screen.trendingMediaScreen.screenContent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.components.RequestToLoginBottomSheet
import com.sanaa.presentation.screen.trendingMediaScreen.MediaListScreenInteractionListener
import com.sanaa.presentation.screen.trendingMediaScreen.TrendingMediaScreenUiState

@Composable
fun TrendingMediaScreenContent(
    title: String,
    state: TrendingMediaScreenUiState,
    interactionListener: MediaListScreenInteractionListener,
    modifier: Modifier = Modifier,
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
        modifier = modifier.systemBarsPadding(),
    ) {

        PaginatedMediaListSectionContent(
            genres = state.genreList,
            mediaList = trendingMedia,
            selectedGenreId = state.selectedGenreId,
            onGenreClick = interactionListener::onGenreClick,
            onMediaClick = { media -> interactionListener.onMediaClick(media.id) },
            onSaveIconClick = interactionListener::onSaveIconClick,
        )

        RequestToLoginBottomSheet(
            isVisible = state.showBottomSheet,
            onDismiss = interactionListener::onDismissBottomSheet,
            onLoginButtonClick = {
                interactionListener.onLoginButtonClick()
            }
        )
    }
}