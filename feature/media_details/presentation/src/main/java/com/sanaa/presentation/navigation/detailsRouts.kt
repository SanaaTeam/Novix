package com.sanaa.presentation.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Destination {
    fun route(): String
}

@Serializable
data class TvShowDetailsScreenRoute(val tvShowId: Int) : Destination {
    override fun route(): String = "tvShow/$tvShowId"

    companion object {
        const val PATTERN = "tvShow/{tvShowId}"
        const val ARG_TV_SHOW_ID = "tvShowId"
    }
}

@Serializable
data class EpisodeDetailsScreenRoute(
    val tvShowId: Int, val seasonNumber: Int, val episodeNumber: Int
) : Destination {
    override fun route(): String = "tvShow/$tvShowId/season/$seasonNumber/episode/$episodeNumber"

    companion object {
        const val PATTERN = "tvShow/{tvShowId}/season/{seasonNumber}/episode/{episodeNumber}"
        const val ARG_TH_SHOW_ID = "tvShowId"
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
data class ReviewsScreenRoute(
    val mediaId: Int,
    val mediaType: MediaTypeParam
) : Destination {
    override fun route(): String = "reviews/$mediaId/${mediaType.name}"

    companion object {
        const val PATTERN = "reviews/{mediaId}/{mediaType}"
        const val ARG_MEDIA_ID = "mediaId"
        const val ARG_MEDIA_TYPE = "mediaType"
    }
}

@Serializable
enum class MediaTypeParam {
    @SerialName("movie")
    MOVIE,

    @SerialName("tv_show")
    TV_SHOW
}

@Serializable
data class ActorScreenRoute(val actorId: Int) : Destination {
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
data class TopTvShowsScreenRoute(val actorId: Int) : Destination {
    override fun route() = "actor/$actorId/top_tv_show"

    companion object {
        const val PATTERN = "actor/{actorId}/top_tv_show"
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

@Serializable
data class GenreMovieScreenRoute(val genreId: Int, val genreName: String) : Destination {
    override fun route() = "genre/movie/$genreId/$genreName"

    companion object {
        const val PATTERN = "genre/movie/{genreId}/{genreName}"
        const val ARG_GENRE_ID = "genreId"
        const val ARG_GENRE_NAME = "genreName"
    }
}


@Serializable
data class GenreTvShowsScreenRoute(val genreId: Int, val genreName: String) : Destination {
    override fun route() = "genre/tvShow/$genreId/$genreName"

    companion object {
        const val PATTERN = "genre/tvShow/{genreId}/{genreName}"
        const val ARG_GENRE_ID = "genreId"
        const val ARG_GENRE_NAME = "genreName"
    }
}