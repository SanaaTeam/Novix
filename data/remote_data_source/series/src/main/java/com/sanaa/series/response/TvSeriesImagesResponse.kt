package com.sanaa.series.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ImagesResponse {
    @SerialName("backdrops")
    val backdrops: List<ImageDto> = emptyList()
}

@Serializable
class ImageDto(
    @SerialName("file_path") val filePath: String,
)