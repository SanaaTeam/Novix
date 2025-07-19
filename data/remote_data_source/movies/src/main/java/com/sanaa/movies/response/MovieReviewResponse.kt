package com.sanaa.movies.response

import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReviewsResponse(
    @SerialName("id") val id: Int,
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<ReviewDto>,
)