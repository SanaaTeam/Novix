package com.sanaa.search.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse<T>(
    @SerialName("page")
    val page: Int = 1,

    @SerialName("results")
    val results: List<T> = emptyList(),

    @SerialName("total_pages")
    val totalPages: Int = 1,

    @SerialName("total_results")
    val totalResults: Int = 0
)