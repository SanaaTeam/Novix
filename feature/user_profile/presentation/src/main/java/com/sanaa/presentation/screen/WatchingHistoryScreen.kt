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
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.designsystem.R
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.components.MediaTabs
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
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import dagger.hilt.android.EntryPointAccessors


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
    interactionListener: MediaTabScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val watchedItems = state.watchingHistory.collectAsLazyPagingItems()

    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        TopBar(
            screenTitle = "Watching history",
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        MediaTabs(
            onTabClick = interactionListener::onMediaTabSelection,
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
            WatchingHistoryGrid(
                items = watchedItems,
                onItemClick = { media ->
                    if (media.mediaTypeUi == selectedMediaType) {
                        interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                    }
                }
            )
        }

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
                            contentDescription = "Movie poster",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                )
            }
        }
    }
}




