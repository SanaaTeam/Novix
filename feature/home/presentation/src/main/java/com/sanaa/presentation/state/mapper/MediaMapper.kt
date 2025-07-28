package com.sanaa.presentation.state.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType
import entity.Movie
import entity.TvSeries

@SuppressLint("DefaultLocale")
fun Movie.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaType = MediaType.MOVIE
)

@SuppressLint("DefaultLocale")
fun TvSeries.toState(): MediaItem = MediaItem(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = String.format("%.1f", imdbRating),
    mediaType = MediaType.TV_SHOW
)