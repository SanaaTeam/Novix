package com.sanaa.vod.dataSource.remote.dto.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorCastCreditDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val movieTitle: String? = null,
    @SerialName("name")
    val tvShowTitle: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("character")
    val character: String? = null,
    @SerialName("credit_id")
    val creditId: String? = null
)