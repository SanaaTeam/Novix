package com.sanaa.presentation.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

open class Destination

@Serializable
data class SeriesDetailsScreenRoute(val seriesId: Int) : Destination()

@Serializable
data class EpisodeDetailsScreenRoute(
    val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int
) : Destination()

@Serializable
data class MovieDetailsScreenRoute(val movieId: Int) : Destination()

@Serializable
data class ActorDetailsScreenRoute(val actorId: Int) : Destination()

@Serializable
data class ReviewsScreenRoute(val mediaId: Int, val mediaType: MediaTypeParam) : Destination()

@Serializable
data class ActorGalleryScreenRoute(val actorId: Int) : Destination()

@Serializable
data class TopMediaActorPicksScreenRoute(val mediaId: Int, val mediaType: MediaTypeParam) :
    Destination()

@Serializable
data class SimilarGenreMediaScreenRoute(val genreId: Int, val mediaType: MediaTypeParam)

@Serializable
enum class MediaTypeParam {
    @SerialName("movie")
    MOVIE,

    @SerialName("tv")
    TV
}