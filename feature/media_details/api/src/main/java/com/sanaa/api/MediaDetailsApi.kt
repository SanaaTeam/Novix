package com.sanaa.api

import android.content.Context

interface MediaDetailsApi {
    fun launch(context: Context, startRoute: StartRoute, id: Int)
    fun navigateToTvGenreDetails(context: Context, genreId: Int, genreName: String)
    fun navigateToMovieGenreDetails(context: Context, genreId: Int, genreName: String)
}

enum class StartRoute {
    TV_SHOW, MOVIE, ACTOR
}