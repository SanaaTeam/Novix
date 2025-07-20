package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class VideoResponseDto(
    @SerialName("id") val id: Int,
    @SerialName("results") val results: List<VideoDto>
)
