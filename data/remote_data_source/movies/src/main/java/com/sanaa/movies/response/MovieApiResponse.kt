package com.sanaa.movies.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResponse<T>(
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<T> = emptyList()
)