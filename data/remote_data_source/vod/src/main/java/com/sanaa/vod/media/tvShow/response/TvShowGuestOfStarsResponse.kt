package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TvSeriesCastResponse(
    @SerialName("cast") val cast: List<ActorDto> = emptyList(),
)

@Serializable
data class TvSeriesGuestOfStarsResponse(
    @SerialName("guest_stars") val guestStars: List<ActorDto> = emptyList(),
)