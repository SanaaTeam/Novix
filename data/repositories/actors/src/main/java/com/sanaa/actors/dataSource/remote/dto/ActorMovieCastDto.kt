package com.sanaa.actors.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorMovieCastDto(
    @SerialName("id") val actorId: Int,

    @SerialName("cast") val cast: List<MovieCastCreditDto>
) {
    @Serializable
    data class MovieCastCreditDto(
        @SerialName("id") val movieId: Int,

        @SerialName("title") val title: String? = null,

        @SerialName("original_title") val originalTitle: String? = null,

        @SerialName("poster_path") val posterPath: String? = null,

        @SerialName("backdrop_path") val backdropPath: String? = null,

        @SerialName("overview") val overview: String? = null,

        @SerialName("release_date") val releaseDate: String? = null,

        @SerialName("vote_average") val voteAverage: Double? = null,

        @SerialName("vote_count") val voteCount: Int? = null,

        @SerialName("popularity") val popularity: Double? = null,

        @SerialName("character") val character: String? = null,

        @SerialName("credit_id") val creditId: String? = null
    )
}