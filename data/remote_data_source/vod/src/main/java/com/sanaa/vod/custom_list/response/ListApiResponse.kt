package com.sanaa.vod.custom_list.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListApiResponse<T>(
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<T> = emptyList(),
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null
)