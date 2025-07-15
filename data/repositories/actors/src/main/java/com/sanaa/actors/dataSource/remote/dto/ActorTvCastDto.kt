package com.sanaa.actors.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorTvCastDto(
    @SerialName("id") val actorId: Int,

    @SerialName("cast") val cast: List<TvCastCreditDto>
) {

    @Serializable
    data class TvCastCreditDto(
        @SerialName("id") val tvId: Int,

        @SerialName("name") val name: String? = null,

        @SerialName("original_name") val originalName: String? = null,

        @SerialName("poster_path") val posterPath: String? = null,

        @SerialName("backdrop_path") val backdropPath: String? = null,

        @SerialName("overview") val overview: String? = null,

        @SerialName("first_air_date") val firstAirDate: String? = null,

        @SerialName("vote_average") val voteAverage: Double? = null,

        @SerialName("vote_count") val voteCount: Int? = null,

        @SerialName("popularity") val popularity: Double? = null,

        @SerialName("character") val character: String? = null,

        @SerialName("episode_count") val episodeCount: Int? = null,

        @SerialName("credit_id") val creditId: String? = null
    )
}