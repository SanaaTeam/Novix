package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.ImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageDto> = emptyList()
)

