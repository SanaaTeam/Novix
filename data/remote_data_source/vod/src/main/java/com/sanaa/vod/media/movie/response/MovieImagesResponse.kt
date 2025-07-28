package com.sanaa.vod.media.movie.response

import com.sanaa.vod.dataSource.remote.dto.ImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageDto> = emptyList()
)