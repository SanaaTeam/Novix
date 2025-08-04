package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.tvShow.TvShowDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreTvShowResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<TvShowDto>,
)