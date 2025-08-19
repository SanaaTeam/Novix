package com.sanaa.presentation.screen.myRating

import com.sanaa.presentation.model.RatedMediaUiModel

data class MyRatingScreenUiState(
    val ratedMovies: List<RatedMediaUiModel> = emptyList(),
    val ratedTvShows: List<RatedMediaUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTab: MyRatingTab = MyRatingTab.ALL,
    val isNoInternetConnection: Boolean = false,
    val snackBarData: SnackData? = null

)
data class SnackData(
    val message: String,
    val isError: Boolean
)

enum class MyRatingTab {
    ALL,
    MOVIES,
    TV_SHOWS
}
enum class MediaTypeUi {
    MOVIE, TV_SHOW
}