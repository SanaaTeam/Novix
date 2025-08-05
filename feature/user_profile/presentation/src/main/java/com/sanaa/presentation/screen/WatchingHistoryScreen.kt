package com.sanaa.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.designsystem.R
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.sanaa.designsystem.design_system.theme.NovixTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.paging.compose.LazyPagingItems
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import com.sanaa.api.MediaDetailsApi
import com.sanaa.api.StartRoute
import com.sanaa.presentation.api.navigation.LocalAppNavController
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.state.MediaTypeUi
import com.sanaa.presentation.state.WatchingHistoryUiState
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.viewmodel.WatchingHistoryViewModel
import dagger.hilt.android.EntryPointAccessors
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import com.sanaa.feature.home.presentation.R as HomeR

@Composable
fun WatchingHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: WatchingHistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current
    val appContext = LocalContext.current.applicationContext

    val detailsApi: MediaDetailsApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .detailsApi()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MediaTabScreenEffect.NavigateToMediaDetails -> {
                    val startRoute = if (effect.mediaTypeUi == MediaTypeUi.MOVIE) StartRoute.MOVIE else StartRoute.SERIES
                    detailsApi.launch(
                        context = navController.context,
                        id = effect.id,
                        startRoute = startRoute
                    )
                }
                is MediaTabScreenEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {
        WatchingHistoryScreenContent(
            state = state.value,
            interactionListener = viewModel,
            modifier = modifier,
        )
    }
}

@Composable
private fun WatchingHistoryScreenContent(
    state: WatchingHistoryUiState,
    interactionListener: WatchingHistoryViewModel,
    modifier: Modifier = Modifier,
) {
    val watchedItems = state.watchingHistory.collectAsLazyPagingItems()

    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        TopBar(
            screenTitle = stringResource(HomeR.string.watching_history),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        WatchingHistoryTabs(
            onTabClick = { tab ->
                val mediaTypeUi = when (tab) {
                    "All" -> null
                    "Movies" -> MediaTypeUi.MOVIE
                    "TV Shows" -> MediaTypeUi.TV_SHOW
                    else -> null
                }
                interactionListener.onMediaTabSelection(mediaTypeUi)
            },
            selectedTab = when (state.selectedMediaTypeUi) {
                null -> "All"
                MediaTypeUi.MOVIE -> "Movies"
                MediaTypeUi.TV_SHOW -> "TV Shows"
            },
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
                    Text(
                        text = stringResource(HomeR.string.no_history_yet),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = com.sanaa.designsystem.design_system.theme.Theme.colors.hint
                    )
                }
            } else {
                WatchingHistoryGrid(
                    items = watchedItems,
                    onItemClick = { media ->
                        if (selectedMediaType == null || media.mediaTypeUi == selectedMediaType) {
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun WatchingHistoryTabs(
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedTab: String = "All",
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WatchingHistoryTabButton(
                text = stringResource(HomeR.string.all),
                onClick = onTabClick,
                isSelected = selectedTab == stringResource(HomeR.string.all),
                modifier = Modifier.weight(1f)
            )
            WatchingHistoryTabButton(
                text = stringResource(HomeR.string.movies),
                onClick = onTabClick,
                isSelected = selectedTab == stringResource(HomeR.string.movies),
                modifier = Modifier.weight(1f)
            )
            WatchingHistoryTabButton(
                text = stringResource(HomeR.string.tv_shows),
                onClick = onTabClick,
                isSelected = selectedTab == stringResource(HomeR.string.tv_shows),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WatchingHistoryTabButton(
    text: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) com.sanaa.designsystem.design_system.theme.Theme.colors.title else com.sanaa.designsystem.design_system.theme.Theme.colors.hint,
    )

    val animatedLineWidthDp by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
    )

    val interactionSource = remember { MutableInteractionSource() }
    
    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick(text) }
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = animatedTextColor,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth(animatedLineWidthDp)
                .background(
                    color = com.sanaa.designsystem.design_system.theme.Theme.colors.primary,
                    shape = RoundedCornerShape(1.dp)
                )
        )
    }
}

@Composable
fun WatchingHistoryGrid(
    items: LazyPagingItems<MediaItem>,
    onItemClick: (MediaItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(
            count = items.itemCount,
            key = { index -> items[index]?.id ?: index }
        ) { index ->
            val item = items[index]
            item?.let { mediaItem ->
            MediaPosterCard(
                    onCardClick = { onItemClick(mediaItem) },
                    posterImage = {
                        Image(
                            painter = painterResource(R.drawable.icon_placeholder_light),
                            contentDescription = stringResource(HomeR.string.movie_poster),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
            }
        }
    }
}




