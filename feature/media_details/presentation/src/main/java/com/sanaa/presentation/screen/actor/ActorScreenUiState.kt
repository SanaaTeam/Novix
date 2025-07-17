package com.sanaa.presentation.screen.actor

import entity.Actor
import entity.Movie
import entity.TvSeries

data class ActorScreenUiState(
    val actor: ActorUiModel = ActorUiModel(),
    val topMovies: List<MovieUiModel> = emptyList(),
    val topTvSeries: List<SeriesUiModel> = emptyList(),
    val profileImages: List<String> = emptyList(),
    val galleryImages: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class ActorUiModel(
    val id: Int = 0,
    val imageUrl: String? = null,
    val name: String = "",
    val region: String? = null,
    val lastShow: String? = null,
    val gender: String = "",
    val department: String? = null,
    val character: String? = null,
    val lifeSpan: String? = null,
    val placeOfBirth: String? = null,
    val biography: String? = null,
)

data class MovieUiModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String? = null,
    val rating: String = "",
)

data class SeriesUiModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String? = null,
    val rating: String = "",
)

fun Actor.toActorUiModel() = ActorUiModel(
    id = id,
    imageUrl = imageUrl,
    name = name,
    region = region,
    lastShow = lastShow,
    gender = if (gender == Actor.Gender.MALE) "male" else "female",
    department = department?.toString(),
    character = character,
    lifeSpan = birthDate?.let { birth -> deathDate?.let { death -> "$birth - $death" } ?: birth }
        .toString(),
    placeOfBirth = placeOfBirth?.toString(),
    biography = biography?.takeIf(String::isNotBlank),
)

fun TvSeries.toSeriesUiModel() = SeriesUiModel(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating.toString(),
)

fun Movie.toMovieUiModel() = MovieUiModel(
    id = id,
    title = title,
    imageUrl = posterImageUrl,
    rating = imdbRating.toString(),
)
