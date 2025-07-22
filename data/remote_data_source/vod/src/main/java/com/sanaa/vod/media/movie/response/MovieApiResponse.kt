package com.sanaa.vod.media.movie.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResponse<T>(
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<T> = emptyList()
)