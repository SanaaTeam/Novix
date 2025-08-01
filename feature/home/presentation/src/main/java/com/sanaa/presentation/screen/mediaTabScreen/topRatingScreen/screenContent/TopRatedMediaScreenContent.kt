package com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen.screenContent

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.home.presentation.R
import com.sanaa.presentation.components.MediaTabs
import com.sanaa.presentation.components.PaginatedMediaListSectionContent
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenInteractionListener
import com.sanaa.presentation.screen.mediaTabScreen.topRatingScreen.TopRatedMediaScreenUiState
import com.sanaa.presentation.state.MediaTypeUi

@Composable
fun TopRatedMediaScreenContent(
    title: String,
    state: TopRatedMediaScreenUiState,
    interactionListener: MediaTabScreenInteractionListener,
    modifier: Modifier = Modifier,
) {
    val topRatedTvShows = state.tvShowList.collectAsLazyPagingItems()
    val topRatedMovies = state.movieList.collectAsLazyPagingItems()
    Column(
        modifier = modifier.padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        NovixTopBar(
            leftContent = {
                TopBarClickableIcon(
                    icon = painterResource(id = R.drawable.icon_back),
                    onClick = interactionListener::onBackClick
                )
            },
            screenTitle = title,
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
            when (selectedMediaType) {

                MediaTypeUi.MOVIE -> {
                    PaginatedMediaListSectionContent(
                        genres = state.movieGenres,
                        mediaList = topRatedMovies,
                        selectedGenreId = state.movieSelectedGenreId,
                        onGenreClick = interactionListener::onMovieGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                }

                MediaTypeUi.TV_SHOW -> {
                    PaginatedMediaListSectionContent(
                        genres = state.tvShowGenres,
                        mediaList = topRatedTvShows,
                        selectedGenreId = state.tvShowSelectedGenreId,
                        onGenreClick = interactionListener::onTvShowGenreClick,
                        onMediaClick = { media ->
                            interactionListener.onMediaClick(media.id, media.mediaTypeUi)
                        },
                        onSaveIconClick = interactionListener::onSaveIconClick,
                    )
                }
            }
        }
    }
}
