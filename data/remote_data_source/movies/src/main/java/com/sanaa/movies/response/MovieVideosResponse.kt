package com.sanaa.movies.response

import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideosResponse(
    @SerialName("id") val id: Int,
    @SerialName("results") val results: List<MovieVideoDto>
)