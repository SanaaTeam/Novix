package com.sanaa.presentation.mapper

import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import entity.Movie
import entity.TvSeries

fun List<Movie>.movieToMedia(): List<MediaItem>{
    return this.map {
        MediaItem(
            id = it.id,
            title = it.title,
            imageUrl = it.posterImageUrl,
            rating = it.imdbRating,
            mediaType = MediaType.MOVIE
        )
    }
}
fun List<TvSeries>.tvShowToMedia(): List<MediaItem>{
    return this.map {
        MediaItem(
            id = it.id,
            title = it.title,
            imageUrl = it.posterImageUrl.orEmpty(),
            rating = it.imdbRating,
            mediaType = MediaType.TV_SHOW
        )
    }
}