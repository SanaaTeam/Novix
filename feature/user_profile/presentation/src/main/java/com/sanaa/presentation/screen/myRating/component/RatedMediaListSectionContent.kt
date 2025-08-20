package com.sanaa.presentation.screen.myRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.designsystem.design_system.component.screen_state_content.EmptyStateComponent
import com.sanaa.feature.userprofile.presentation.R
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.myRating.MyRatingScreenUiState
import com.sanaa.presentation.screen.myRating.MyRatingTab

@Composable
fun RatedMediaListSectionContent(
    state: MyRatingScreenUiState,
    onTabSelected: (MyRatingTab) -> Unit,
    onDeleteRatingClick: (mediaId: Int, mediaType: MediaTypeUi) -> Unit,
    onCardClick: (mediaId: Int, mediaType: MediaTypeUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = MyRatingTab.entries.toTypedArray()) { tab ->
                val tabText = when (tab) {
                    MyRatingTab.ALL -> stringResource(R.string.all)
                    MyRatingTab.MOVIES -> stringResource(R.string.movies)
                    MyRatingTab.TV_SHOWS -> stringResource(R.string.tv_shows)
                }
                ToggleableChip(
                    text = tabText,
                    onClick = { onTabSelected(tab) },
                    isSelected = state.selectedTab == tab,
                )
            }
        }

        val mediaListToShow = when (state.selectedTab) {
            MyRatingTab.ALL -> state.ratedMovies + state.ratedTvShows
            MyRatingTab.MOVIES -> state.ratedMovies
            MyRatingTab.TV_SHOWS -> state.ratedTvShows
        }

        if (mediaListToShow.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyStateComponent(messageText = stringResource(R.string.empty_rating))
            }
        } else {
            RatedMediaListGrid(
                onDeleteIconClick = onDeleteRatingClick,
                onCardClick = onCardClick,
                mediaList = mediaListToShow,
                modifier = Modifier.fillMaxSize(),
                isScrollEnabled = true,
            )
        }
    }
}