package com.sanaa.movies.response

import com.sanaa.movies.dataSource.remote.dto.ActorDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieCastResponse(
    @SerialName("cast") val cast: List<ActorDto> = emptyList(),
)