package com.sanaa.presentation.navigation

import com.sanaa.presentation.model.MediaTypeUiModel
import kotlinx.serialization.Serializable

interface Destination

@Serializable
data class TvShowScreenRoute(val tvShowId: Int) : Destination

@Serializable
data class EpisodeDetailsScreenRoute(
    val tvShowId: Int, val seasonNumber: Int, val episodeNumber: Int,
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
data class TopTvShowsScreenRoute(val actorId: Int) : Destination

@Serializable
data class ActorGalleryScreenRoute(val actorId: Int) : Destination

@Serializable
data class GenreMovieScreenRoute(val genreId: Int, val genreName: String) : Destination

@Serializable
data class GenreTvShowsScreenRoute(val genreId: Int, val genreName: String) : Destination