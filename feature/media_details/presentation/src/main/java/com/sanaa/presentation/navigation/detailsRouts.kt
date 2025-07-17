package com.sanaa.presentation.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Destination {
    fun route(): String
}

@Serializable
data class SeriesDetailsScreenRoute(val seriesId: Int) : Destination {
    override fun route(): String = "series/$seriesId"

    companion object {
        const val PATTERN = "series/{seriesId}"
        const val ARG_SERIES_ID = "seriesId"
    }
}

@Serializable
data class EpisodeDetailsScreenRoute(
    val seriesId: Int, val seasonNumber: Int, val episodeNumber: Int
) : Destination {
    override fun route(): String = "series/$seriesId/season/$seasonNumber/episode/$episodeNumber"

    companion object {
        const val PATTERN = "series/{seriesId}/season/{seasonNumber}/episode/{episodeNumber}"
        const val ARG_SERIES_ID = "seriesId"
        const val ARG_SEASON_NUMBER = "seasonNumber"
        const val ARG_EPISODE_NUMBER = "episodeNumber"
    }
}

@Serializable
data class MovieDetailsScreenRoute(val movieId: Int) : Destination {
    override fun route(): String = "movie/$movieId"

    companion object {
        const val PATTERN = "movie/{movieId}"
        const val ARG_MOVIE_ID = "movieId"
    }
}

@Serializable
data class ReviewsScreenRoute(val seriesId: Int) : Destination {
    override fun route(): String = "reviews/$seriesId"

    companion object {
        const val PATTERN = "reviews/{seriesId}"
        const val ARG_SERIES_ID = "seriesId"
    }
}

@Serializable
data class TopMediaActorPicksScreenRoute(val mediaId: Int, val mediaType: MediaTypeParam) :
    Destination {
    override fun route(): String = "top_media_actor_picks/$mediaId/$mediaType"

    companion object {
        const val PATTERN = "top_media_actor_picks/{mediaId}/{mediaType}"
        const val ARG_MEDIA_ID = "mediaId"
        const val ARG_MEDIA_TYPE = "mediaType"
    }
}

@Serializable
data class SimilarGenreMediaScreenRoute(val genreId: Int, val mediaType: MediaTypeParam) :
    Destination {
    override fun route(): String = "similar_genre_media/$genreId/$mediaType"

    companion object {
        const val PATTERN = "similar_genre_media/{genreId}/{mediaType}"
        const val ARG_GENRE_ID = "genreId"
        const val ARG_MEDIA_TYPE = "mediaType"
    }
}

@Serializable
enum class MediaTypeParam {
    @SerialName("movie")
    MOVIE,

    @SerialName("tv")
    TV
}

@Serializable
data class ActorDetailsScreenRoute(val actorId: Int) : Destination {
    override fun route() = "actor/$actorId"

    companion object {
        const val PATTERN = "actor/{actorId}"
        const val ARG_ACTOR_ID = "actorId"
    }
}

@Serializable
data class TopMoviesScreenRoute(val actorId: Int) : Destination {
    override fun route() = "actor/$actorId/top_movies"

    companion object {
        const val PATTERN = "actor/{actorId}/top_movies"
        const val ARG_ACTOR_ID = "actorId"
    }
}

@Serializable
data class TopSeriesScreenRoute(val actorId: Int) : Destination {
    override fun route() = "actor/$actorId/top_series"

    companion object {
        const val PATTERN = "actor/{actorId}/top_series"
        const val ARG_ACTOR_ID = "actorId"
    }
}

@Serializable
data class ActorGalleryScreenRoute(val actorId: Int) : Destination {
    override fun route() = "actor/$actorId/gallery"

    companion object {
        const val PATTERN = "actor/{actorId}/gallery"
        const val ARG_ACTOR_ID = "actorId"
    }
}