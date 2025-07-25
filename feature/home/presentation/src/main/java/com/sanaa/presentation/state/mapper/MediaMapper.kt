package com.sanaa.presentation.state.mapper

import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import entity.Movie
import entity.TvSeries

fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating,
    mediaType = MediaType.MOVIE
)

fun TvSeries.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating,
    mediaType = MediaType.TV_SHOW
)