package com.sanaa.tvapp.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.screen_state_content.NetworkDisconnectionContact
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.presentation.screens.home.component.HomeScreenLoading
import com.sanaa.tvapp.presentation.screens.home.component.MediaTab
import com.sanaa.tvapp.presentation.screens.home.component.PopularMoviesCarousel
import com.sanaa.tvapp.presentation.screens.home.component.Title
import com.sanaa.tvapp.presentation.screens.home.tabRoutes.HomeMoviesTapRoute
import com.sanaa.tvapp.presentation.screens.home.tabRoutes.HomeTvShowsTapRoute
import com.sanaa.tvapp.presentation.screens.home.component.TitleShimmer
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MovieDetails
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.TvShowDetails
import com.sanaa.tvapp.state.MediaItem
import com.sanaa.designsystem.R as dosingSystemResource
import com.sanaa.tvapp.R as tvResource

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
fun HomeScreenContent(state: HomeScreenUiState, upcomingMovies: LazyPagingItems<MediaItem>) {
    val sidePaddings = 36.dp
    val scrollState = rememberScrollState()
    val mainTvNavController = LocalAppNavController.current
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 24.dp)
                    .align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(tvResource.drawable.novix_logo),
                    contentDescription = null,
                )

                Text(
                    stringResource(tvResource.string.app_name),
                    color = Theme.colors.title,
                    style = Theme.textStyle.title.medium,
                )
            }
        }

        PopularMoviesCarousel(
            modifier = Modifier.padding(
                start = sidePaddings,
                end = sidePaddings,
                bottom = 16.dp
            ),
            mediaItems = state.popularMedia,
            onShowDetails = {}
        )

        MediaTab(sidePaddings, navController)

        NavHost(navController = navController, startDestination = HomeMoviesTapRoute) {
            composable(route = HomeMoviesTapRoute::class) {
                HomeMovies(state, upcomingMovies){id->
                    mainTvNavController.navigate(ScreensRoute.MovieDetails(id))
                }
            }

            composable(route = HomeTvShowsTapRoute::class) {
                HomeTvShows(state, upcomingMovies){id->
                    mainTvNavController.navigate(ScreensRoute.TvShowDetails(id))
                }
            }
        }
    }
}


@Composable
fun HomeMovies(
    state: HomeScreenUiState,
    upcomingMovies: LazyPagingItems<MediaItem>,
    onItemClick: (Int) -> Unit
) {
    Column {
        MediaSection(title = stringResource(tvResource.string.top_rated)) {
            items(
                items = state.topRatingMovies,
                key = { it.id }
            ) {
                ImageList(
                    it,
                    onItemClick = {id->
                        onItemClick(id)
                    }
                )
            }
        }

        if (state.continueWatchingMovies.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.watching_history)) {
                items(
                    items = state.continueWatchingMovies,
                    key = { it.id }
                ) {
                    ImageList(
                        it,
                        onItemClick = {id->
                            onItemClick(id)
                        }
                    )
                }
            }
        }

        MediaSection(title = stringResource(tvResource.string.up_upcoming)) {
            items(
                count = upcomingMovies.itemCount,
            ) { index ->
                upcomingMovies[index]?.let {
                    ImageList(
                        it,
                        onItemClick = {id->
                            onItemClick(id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTvShows(
    state: HomeScreenUiState,
    upcomingMovies: LazyPagingItems<MediaItem>,
    onItemClick: (Int) -> Unit
) {
    Column {
        MediaSection(title = stringResource(tvResource.string.top_rated)) {
            items(
                items = state.topRatingTvShows,
                key = { it.id }
            ) {
                ImageList(
                    it,
                    onItemClick = {id->
                        onItemClick(id)
                    }
                )
            }
        }

        if (state.continueWatchingTvShows.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.watching_history)) {
                items(
                    items = state.continueWatchingTvShows,
                    key = { it.id }
                ) {
                    ImageList(
                        it,
                        onItemClick = {id->
                            onItemClick(id)
                        }
                    )
                }
            }
        }

        MediaSection(title = stringResource(tvResource.string.up_upcoming)) {
            items(
                count = upcomingMovies.itemCount,
            ) { index ->
                upcomingMovies[index]?.let {
                    ImageList(
                        it,
                        onItemClick={id->
                            onItemClick(id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaSection(
    title: String,
    content: LazyListScope.() -> Unit,
) {
    val sidePaddings = 36.dp

    Title(
        modifier = Modifier.padding(horizontal = sidePaddings, vertical = 16.dp),
        title = title
    )

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = sidePaddings, end = sidePaddings, bottom = 12.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}


@Composable
@OptIn(ExperimentalTvMaterial3Api::class)
 fun ImageList(
    item: MediaItem,
    onItemClick:(Int)-> Unit
) {
    Card(
        modifier = Modifier
            .width(153.dp)
            .height(231.dp),
        colors = CardDefaults.colors(containerColor = Theme.colors.surfaceHigh),
        onClick = {
            onItemClick(item.id)
        },
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
        ) {
            OnBlurContent(
                hintText = stringResource(dosingSystemResource.string.unsuitable_image),
                textStyle = Theme.textStyle.body.small.copy(
                    color = Color(0x99FFFFFF)
                ),
                iconSize = 24.dp,
                icon = painterResource(dosingSystemResource.drawable.icon_eye_slash),
            )
        }
    }
}
