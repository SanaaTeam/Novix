package com.sanaa.series.response

import com.sanaa.series.dto.ImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImagesResponse {
    @SerialName("backdrops")
    val backdrops: List<ImageDto> = emptyList()
}

