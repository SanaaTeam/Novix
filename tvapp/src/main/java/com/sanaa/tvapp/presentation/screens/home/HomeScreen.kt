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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
                HomeScreenContent(state)
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

        TitleShimmer()
        ImageListShimmer()

        TitleShimmer()
        ImageListShimmer()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeScreenUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        FeaturedCarousel(state.featuredCarousel) {}

        Title(stringResource(R.string.top_rated))

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 12.dp
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

        Title(stringResource(R.string.watching_history))

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 12.dp
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
        scale = CardDefaults.scale(focusedScale = 1.01f),
    ) {
        RemoteBlurredSensitiveImage(
            imageUrl = item.imageUrl ?: "",
            contentDescription = item.title
        )
    }
}
