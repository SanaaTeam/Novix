package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.VideoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowVideosResponse(
    @SerialName("id") val id: Int,
    @SerialName("results") val results: List<VideoDto>
)