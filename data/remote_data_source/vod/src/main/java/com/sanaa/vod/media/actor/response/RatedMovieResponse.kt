package com.sanaa.vod.media.actor.response

import com.sanaa.vod.dataSource.remote.dto.MovieDto
import kotlinx.serialization.SerialName

data class RatedMovieResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MovieDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)
