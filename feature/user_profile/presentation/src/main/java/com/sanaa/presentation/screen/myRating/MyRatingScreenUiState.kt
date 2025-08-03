package com.sanaa.presentation.screen.myRating

import com.sanaa.presentation.model.RatedMediaUiModel

data class MyRatingScreenUiState(
    val ratedMovies: List<RatedMediaUiModel> = emptyList(),
    val ratedTvShows: List<RatedMediaUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: MyRatingTab = MyRatingTab.ALL,
    val error: String? = null
)

enum class MyRatingTab {
    ALL,
    MOVIES,
    TV_SHOWS
}