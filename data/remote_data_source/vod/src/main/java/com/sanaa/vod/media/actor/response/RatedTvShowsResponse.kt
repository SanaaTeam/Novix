package com.sanaa.vod.media.actor.response

import com.sanaa.vod.dataSource.remote.dto.TvShowDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatedTvShowsResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<TvShowDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)