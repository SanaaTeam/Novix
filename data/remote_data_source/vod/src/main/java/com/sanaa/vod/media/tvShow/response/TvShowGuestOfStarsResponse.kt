package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowCastResponse(
    @SerialName("cast") val cast: List<ActorDto> = emptyList(),
)

@Serializable
data class TvShowGuestOfStarsResponse(
    @SerialName("guest_stars") val guestStars: List<ActorDto> = emptyList(),
)