package com.sanaa.tvapp.presentation.screens.mediaDetails.model.mapper

import android.annotation.SuppressLint
import com.sanaa.presentation.model.mapper.toUiModel
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import entity.Movie

@SuppressLint("DefaultLocale")
fun Movie.toDetailsUiModel():MovieDetailsUiModel{
    return MovieDetailsUiModel(
        id = id,
        title = title,
        overview = overview,
        rating = String.format("%.1f", imdbRating),
        releaseDate = releaseDate.toString(),
        duration = duration,
        genres = genres.map { it.toUiModel() },
        trailerUrl = trailerUrl,
        posterUrl = posterImageUrl
    )
}