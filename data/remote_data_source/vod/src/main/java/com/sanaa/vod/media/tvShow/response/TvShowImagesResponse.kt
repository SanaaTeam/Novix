package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.ImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImagesResponse {
    @SerialName("backdrops")
    val backdrops: List<ImageDto> = emptyList()
}

