package com.sanaa.presentation.screen.genreTvShows

sealed class GenreTvShowsEffects {
    object NavigateBack : GenreTvShowsEffects()
    data class NavigateToTvShowDetails(val id: Int) : GenreTvShowsEffects()
}