package com.sanaa.presentation.navigation

import com.sanaa.presentation.model.MediaTypeUiModel
import kotlinx.serialization.Serializable

interface Destination

@Serializable
data class SeriesScreenRoute(val seriesId: Int) : Destination

@Serializable
data class EpisodeDetailsScreenRoute(
    val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int,
) : Destination

@Serializable
data class MovieDetailsScreenRoute(val movieId: Int) : Destination

@Serializable
data class ReviewsScreenRoute(val mediaId: Int, val mediaType: MediaTypeUiModel) : Destination

@Serializable
data class ActorScreenRoute(val actorId: Int) : Destination

@Serializable
data class TopMoviesScreenRoute(val actorId: Int) : Destination

@Serializable
data class TopSeriesScreenRoute(val actorId: Int) : Destination

@Serializable
data class ActorGalleryScreenRoute(val actorId: Int) : Destination

@Serializable
data class GenreMoviesScreenRoute(val categoryId: Int, val categoryName: String) : Destination

@Serializable
data class GenreTvShowsScreenRoute(val genreId: Int, val genreName: String) : Destination