package com.sanaa.movies.response

import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<MovieImagesDto> = emptyList()
)