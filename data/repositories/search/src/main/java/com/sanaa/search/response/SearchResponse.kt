package com.sanaa.search.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse<T>(
    @SerialName("page")
    val page: Int,

    @SerialName("results")
    val results: List<T>,

    @SerialName("total_pages")
    val totalPages: Int,

    @SerialName("total_results")
    val totalResults: Int
)
