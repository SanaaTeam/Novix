package com.sanaa.series.response

import com.sanaa.series.dto.VideoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesVideosResponse(
    @SerialName("id") val id: Int,
    @SerialName("results") val results: List<VideoDto>
)