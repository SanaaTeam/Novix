package com.sanaa.series.response

import com.sanaa.series.dto.TvSeriesDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreTvSeriesResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<TvSeriesDto>,
)