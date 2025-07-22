package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreTvSeriesResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<TvShowDto>,
)