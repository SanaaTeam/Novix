package com.sanaa.vod.media.movie.response

import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponse(
    @SerialName("cast") val cast: List<ActorDto> = emptyList(),
)