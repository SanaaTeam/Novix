package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowReviewsResponse(
    @SerialName("id") val id: Int,
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<ReviewDto>,
)