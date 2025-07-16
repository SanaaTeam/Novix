package com.sanaa.presentation.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Destination {
    fun route(): String
}


@Serializable
data class SeriesDetailsScreenRoute(val seriesId: Int) : Destination {
    override fun route(): String = "series/$seriesId"
}

@Serializable
data class EpisodeDetailsScreenRoute(
    val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int
) : Destination {
    override fun route(): String = "series/$seriesId/season/$seasonNumber/episode/$episodeNumber"
}

@Serializable
data class MovieDetailsScreenRoute(val movieId: Int) : Destination {
    override fun route(): String = "movie/$movieId"
}

@Serializable
data class ActorDetailsScreenRoute(val actorId: Int) : Destination {
    override fun route(): String = "actor/$actorId"
}

@Serializable
data class ReviewsScreenRoute(val mediaId: Int, val mediaType: MediaTypeParam) : Destination {
    override fun route(): String = "reviews/$mediaId/$mediaType"
}

@Serializable
data class ActorGalleryScreenRoute(val actorId: Int) : Destination {
    override fun route(): String = "actor/$actorId/gallery"
}

@Serializable
data class TopMediaActorPicksScreenRoute(val mediaId: Int, val mediaType: MediaTypeParam) :
    Destination {
    override fun route(): String = "top_media_actor_picks/$mediaId/$mediaType"
}

@Serializable
data class SimilarGenreMediaScreenRoute(val genreId: Int, val mediaType: MediaTypeParam) :
    Destination {
    override fun route(): String = "similar_genre_media/$genreId/$mediaType"
}

@Serializable
enum class MediaTypeParam {
    @SerialName("movie")
    MOVIE,

    @SerialName("tv")
    TV
}