package com.sanaa.tvapp.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.home.component.FeaturedCarousel
import com.sanaa.tvapp.presentation.screens.home.component.FeaturedCarouselShimmer
import com.sanaa.tvapp.presentation.screens.home.component.Title
import com.sanaa.tvapp.presentation.screens.home.component.TitleShimmer
import com.sanaa.tvapp.state.MediaItem
import com.sanaa.tvapp.util.shimmerEffect.PlaceholderWithShimmerEffect

@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val state by homeScreenViewModel.state.collectAsStateWithLifecycle()
    val upcomingMovies = state.upcomingMovies.collectAsLazyPagingItems()
    when {
        state.isNoInternet -> {
            NetworkDisconnectionContact(
                modifier = Modifier
                    .fillMaxSize()

                    .verticalScroll(scrollState),
                onRetryClick = {}
            )
        }

        state.isLoading -> {
            HomeScreenLoading(
                modifier = Modifier
                    .verticalScroll(scrollState),
            )
        }

        else -> {
            HomeScreenContent(state, upcomingMovies)
        }
    }
}

@Composable
private fun HomeScreenLoading(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        FeaturedCarouselShimmer()

        repeat(3) {
            TitleShimmer()
            ImageListShimmer()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeScreenUiState, upcomingMovies: LazyPagingItems<MediaItem>) {
    val scrollState = rememberScrollState()
    val sidePaddings = 36.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        FeaturedCarousel(
            modifier = Modifier.padding(
                start = sidePaddings,
                end = sidePaddings,
                top = 24.dp,
                bottom = 16.dp
            ),
            state.featuredCarousel
        ) {}

        Title(
            modifier = Modifier.padding(horizontal = sidePaddings, vertical = 16.dp),
            title = stringResource(R.string.top_rated)
        )

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = sidePaddings, end = sidePaddings, bottom = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = state.topRatingMedia,
                key = { it.id }
            ) {
                ImageList(it)
            }
        }

        if (state.continueWatchingMedia.isNotEmpty()) {
            Title(
                modifier = Modifier.padding(horizontal = sidePaddings, vertical = 16.dp),
                title = stringResource(R.string.watching_history)
            )

            LazyRow(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = sidePaddings, end = sidePaddings, bottom = 12.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.continueWatchingMedia,
                    key = { it.id }
                ) {
                    ImageList(it)
                }
            }
        }

        Title(
            modifier = Modifier.padding(horizontal = sidePaddings, vertical = 16.dp),
            title = stringResource(R.string.up_upcoming)
        )

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = sidePaddings, end = sidePaddings, bottom = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                count = upcomingMovies.itemCount,
            ) { index ->
                upcomingMovies[index]?.let {
                    ImageList(it)
                }
            }
        }
    }
}

@Composable
fun ImageListShimmer() {
    Row {
        repeat(10) {
            PlaceholderWithShimmerEffect(
                modifier = Modifier.padding(end = 20.dp, bottom = 24.dp),
                width = 153.dp,
                height = 231.dp,
                borderColor = Color.Transparent,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalTvMaterial3Api::class)
private fun ImageList(item: MediaItem) {
    Card(
        modifier = Modifier
            .width(153.dp)
            .height(231.dp),
        onClick = {},
        colors = CardDefaults.colors(),
        border = CardDefaults.border(
            border = Border.None,
            focusedBorder = Border(
                border = BorderStroke(
                    width = 3.dp,
                    color = Theme.colors.primary,
                ),
                shape = RoundedCornerShape(12.dp),
            ),
        ),
        scale = CardDefaults.scale(focusedScale = 1.05f),
    ) {
        RemoteBlurredSensitiveImage(
            imageUrl = item.imageUrl ?: "",
            contentDescription = item.title
        )
    }
}
