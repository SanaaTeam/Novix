package com.sanaa.tvapp.presentation.screens.genreTvShows

sealed class GenreTvShowsEffects {
    object NavigateBack : GenreTvShowsEffects()
    data class NavigateToTvShowDetails(val id: Int) : GenreTvShowsEffects()
}