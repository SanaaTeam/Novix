package com.sanaa.presentation.screen.myRating.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.chips.ToggleableChip
import com.sanaa.presentation.screen.myRating.MyRatingScreenUiState
import com.sanaa.presentation.screen.myRating.MyRatingTab

@Composable
fun RatedMediaListSectionContent(
    state: MyRatingScreenUiState,
    onTabSelected: (MyRatingTab) -> Unit,
    onDeleteRatingClick: (mediaId: Int, mediaType: String) -> Unit,
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
                    MyRatingTab.ALL -> "All"
                    MyRatingTab.MOVIES -> "Movies"
                    MyRatingTab.TV_SHOWS -> "TV shows"
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

        RatedMediaListGrid(
            mediaList = mediaListToShow,
            onDeleteIconClick = onDeleteRatingClick,
            modifier = Modifier.fillMaxSize(),
            isScrollEnabled = true
        )
    }
}