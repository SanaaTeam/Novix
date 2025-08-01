package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class EpisodeDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("overview") val overview: String? = null,
    @SerialName("season_number") val seasonNumber: Int = 0,
    @SerialName("episode_number") val episodeNumber: Int = 0,
    @SerialName("vote_average") val voteAverage: Float? = null,
    @SerialName("air_date") val airDate: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("still_path") val stillPath: String? = null,
    @SerialName("rating") val rating: Float? = null,
)
