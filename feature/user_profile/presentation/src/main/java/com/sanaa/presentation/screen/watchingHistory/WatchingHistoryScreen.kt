package com.sanaa.presentation.screen.watchingHistory

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.designsystem.design_system.component.poster.MediaPosterCard
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.navigation.LocalNavControllerProvider
import com.sanaa.presentation.api.navigation.ProfileApiEntryPoint
import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.myRating.component.RemoteImagePlaceholder
import dagger.hilt.android.EntryPointAccessors


@Composable
fun WatchingHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingHistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavControllerProvider.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, ProfileApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WatchingHistoryScreenEffect.NavigateToMediaDetails -> {
                    val startRoute = if (effect.mediaTypeUi == MediaTypeUi.MOVIE) StartRoute.MOVIE else StartRoute.SERIES
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = startRoute
                    )
                }
                is WatchingHistoryScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

        WatchingHistoryScreenContent(
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )

}

@Composable
private fun WatchingHistoryScreenContent(
    state: WatchingHistoryUiState,
    interactionListener: WatchingHistoryInteractionListener,
    modifier: Modifier = Modifier,
) {
    val watchedItems = state.watchingHistory.collectAsLazyPagingItems()

    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        TopBar(
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = { interactionListener.onBackClick() }
                )
            },
            screenTitle = stringResource(id = R.string.watching_history),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        WatchingHistoryTabs(
            onTabClick = { mediaTypeUi ->
                interactionListener.onMediaTabSelection(mediaTypeUi)
            },
            selectedTab = state.selectedMediaTypeUi,
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedContent(
            targetState = state.selectedMediaTypeUi,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, delayMillis = 150))
                    .togetherWith(fadeOut(animationSpec = tween(150)))
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { selectedMediaType ->
            if (watchedItems.itemCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = stringResource(id = R.string.no_history_yet),
                        style = Theme.textStyle.body.large,
                        textAlign = TextAlign.Center,
                        color = Theme.colors.hint
                    )
                }
            } else {
                WatchingHistoryGrid(
                    items = watchedItems,
                    onItemClick = { media ->
                        if (selectedMediaType == null || media.mediaTypeUi == selectedMediaType) {
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        }
                    },
                    onSaveIconClick = { media ->
                        interactionListener.onSaveIconClick(media)
                    },
                )
            }
        }

    }
}

@Composable
fun WatchingHistoryTabs(
    onTabClick: (MediaTypeUi?) -> Unit,
    modifier: Modifier = Modifier,
    selectedTab: MediaTypeUi? = null,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        item {
            ToggleableChip(
                text = stringResource(id = R.string.all),
                onClick = { onTabClick(null) },
                isSelected = selectedTab == null,
            )
        }
        item {
            ToggleableChip(
                text = stringResource(id = R.string.movies),
                onClick = { onTabClick(MediaTypeUi.MOVIE) },
                isSelected = selectedTab == MediaTypeUi.MOVIE,
            )
        }
        item {
            ToggleableChip(
                text = stringResource(id = R.string.tv_shows),
                onClick = { onTabClick(MediaTypeUi.TV_SHOW) },
                isSelected = selectedTab == MediaTypeUi.TV_SHOW,
            )
        }
    }
}

@Composable
fun WatchingHistoryGrid(
    onItemClick: (MediaItemUiModel) -> Unit,
    onSaveIconClick: (MediaItemUiModel) -> Unit,
    items: LazyPagingItems<MediaItemUiModel>,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 158.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)

    ) {
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id ?: index }
        ) { index ->
            val item = items[index]
            item?.let { mediaItem ->
                MediaPosterCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                    onCardClick = { onItemClick(mediaItem) },
                    topLeftContent = {
                        SaveIconChip(
                            isSaved = mediaItem.isSaved,
                            onClick = { onSaveIconClick(mediaItem) }
                        )
                    },
                    posterImage = {
                        RemoteBlurredSensitiveImage(
                            imageUrl = mediaItem.imageUrl.orEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            sensitiveContentThreshold = 0.2f,
                            contentDescription = "poster",
                            placeholderContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            errorContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                        ) {
                            OnBlurContent(
                                hintText = stringResource(id = R.string.unsuitable_image),
                                textStyle = Theme.textStyle.body.small.copy(
                                    color = Color(0x99FFFFFF)
                                ),
                                iconSize = 24.dp,
                                icon = painterResource(R.drawable.icon_eye_slash),
                            )
                        }
                    }
                )
            }
        }
    }
}




