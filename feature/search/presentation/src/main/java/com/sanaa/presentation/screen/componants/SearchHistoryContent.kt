package com.sanaa.presentation.screen.componants

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.button.NovixTextButton
import com.sanaa.presentation.screen.componants.cards.MovieSeriesPosterCard
import com.sanaa.presentation.screen.componants.cards.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel

@Composable
fun SearchHistoryContent(
    interactionsListener: SearchScreenInteractionsListener,
    recentSearches: List<RecentSearchUiModel> = emptyList(),
    recentViewed: List<RecentViewedUiModel> = emptyList(),
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            recentSearches.isEmpty() && recentViewed.isEmpty(),
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) {
            when (it) {
                true -> EmptyState()
                false -> ContentState(
                    recentViewed,
                    interactionsListener,
                    recentSearches,
                    onMediaClick
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        EmptySearchState(
            icon = painterResource(id = com.sanaa.presentation.R.drawable.icon_search),
            text = stringResource(id = R.string.empty_search_message)
        )
    }
}

@Composable
private fun ContentState(
    recentViewed: List<RecentViewedUiModel>,
    interactionsListener: SearchScreenInteractionsListener,
    recentSearches: List<RecentSearchUiModel>,
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier, contentPadding = PaddingValues(bottom = 24.dp, top = 12.dp)
    ) {
        if (recentViewed.isNotEmpty()) {
            item {
                SectionHeader(
                    title = stringResource(R.string.recent_viewed),
                    actionText = stringResource(R.string.clear_all),
                    onActionClick = interactionsListener::onClearRecentViewClicked
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, bottom = 24.dp
                    )
                ) {
                    itemsIndexed(recentViewed) { _, item ->
                        MediaPoster(
                            item,
                            onMediaClick = {
                                if (item.mediaType == "MOVIE")
                                    onMediaClick(StartRoute.MOVIE, item.id)
                                else
                                    onMediaClick(StartRoute.SERIES, item.id)
                            },
                            interactionsListener,
                        )
                    }
                }
            }
        }

        if (recentSearches.isNotEmpty()) {
            item {
                SectionHeader(
                    title = stringResource(R.string.recent_search),
                    actionText = stringResource(R.string.clear_all),
                    onActionClick = interactionsListener::onClearRecentSearchClicked,
                )
            }

            itemsIndexed(
                recentSearches, key = { _, item -> item.id }) { index, item ->
                RecentSearchItem(
                    text = item.title,
                    onDeleteClicked = {
                        interactionsListener.onDeleteRecentSearchItem(item.id)
                    },
                    onRecentSearchItemClicked = {
                        interactionsListener.onRecentSearchItemClicked(item.title)
                    },
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                )
                if (index != recentSearches.lastIndex) {
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun MediaPoster(
    item: RecentViewedUiModel,
    onMediaClick: () -> Unit,
    interactionsListener: SearchScreenInteractionsListener,
) {

    MovieSeriesPosterCard(
        onCardClick = onMediaClick,
        modifier = Modifier
            .width(158.dp)
            .height(210.dp),
        boastImage = {
            RemoteBlurredHaramImageViewer(
                imageUrl = item.imageUrl,
                modifier = Modifier.fillMaxWidth(),
                blurRadius = 150,
                haramThreshold = 0.2f,
                nonHaramThreshold = 0.7f,
                placeholderContent = {
                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                },
                errorContent = {
                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                },
                contentDescription = null,
            ) {
                OnBlurContent(
                    hintText = stringResource(com.sanaa.presentation.R.string.unsuitable_image),
                    textStyle = Theme.textStyle.body.small.copy(
                        color = Color(0x99FFFFFF)
                    ),
                    iconSize = 24.dp,
                    icon = painterResource(R.drawable.icon_eye_slash),
                )
            }
        },
        topLeftContent = {
            SaveIconChip(
                onClick = {}
            )
        },
    )
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(color = Theme.colors.stroke)
    )
}

@Composable
fun SectionHeader(
    title: String, actionText: String, onActionClick: () -> Unit, modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.body,
            modifier = Modifier.weight(1f)
        )
        NovixTextButton(
            text = actionText, onClick = onActionClick, isLoading = false, isEnabled = true
        )
    }
}