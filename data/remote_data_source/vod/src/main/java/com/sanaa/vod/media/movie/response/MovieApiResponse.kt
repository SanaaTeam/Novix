package com.sanaa.vod.media.movie.response

import com.sanaa.vod.dataSource.remote.dto.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResponse<T>(
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<T> = emptyList(),
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null
)