package com.sanaa.vod.dataSource.remote.dto.tvShow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("season_number")
    val seasonNumber: Int = 0,
    @SerialName("episodes")
    val episodes: List<EpisodeDto> = emptyList(),
)
