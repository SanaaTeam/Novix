package com.sanaa.presentation.screen.componants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.screen.SearchScreenInteractionsListener
import com.sanaa.presentation.screen.state.RecentSearchUiModel
import com.sanaa.presentation.screen.state.RecentViewedUiModel

@Composable
fun SearchHistoryContent(
    interactionsListener: SearchScreenInteractionsListener,
    recentSearches: List<RecentSearchUiModel> = emptyList(),
    recentViewed: List<RecentViewedUiModel> = emptyList(),
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            recentSearches.isEmpty() && recentViewed.isEmpty(), enter = fadeIn(), exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                EmptySearchState(
                    icon = painterResource(id = R.drawable.empty_search),
                    text = stringResource(id = R.string.empty_search_message)
                    )
            }
        }
        AnimatedVisibility(
            recentSearches.isNotEmpty() || recentViewed.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
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
                                MovieSeriesPosterCard(
                                    modifier = Modifier
                                        .width(158.dp)
                                        .height(210.dp),
                                    boastImage = {
                                        RemoteCensoredImageViewer(
                                            imageUrl = item.imageUrl,
                                            modifier = Modifier,
                                            contentScale = ContentScale.Crop,
                                            blurRadius = 150,
                                            sfwThreshold = 0.75f,
                                            nsfwThreshold = 0.15f,
                                            contentDescription = null,
                                            placeholderBackgroundColor = Theme.colors.surface,
                                            hintText = stringResource(com.sanaa.presentation.R.string.unsuitable_image),
                                            textStyle = Theme.textStyle.body.small,
                                            iconSize = 24.dp,
                                        )
                                    },
                                    topLeftContent = {
                                        SaveIconChip(
                                            onClick = interactionsListener::onSaveIconClicked
                                        )
                                    },
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
                            Box(
                                modifier = Modifier
                                    .animateItem(
                                        fadeInSpec = null,
                                        fadeOutSpec = null
                                    )
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .height(1.dp)
                                    .background(color = Theme.colors.stroke)
                            )
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun SectionHeader(
    title: String, actionText: String, onActionClick: () -> Unit, modifier: Modifier = Modifier
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
        TextButton(
            text = actionText, onClick = onActionClick, isLoading = false, isEnabled = true
        )
    }
}