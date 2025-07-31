package com.sanaa.vod.dataSource.remote.search.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowSearchDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String? = null,

    @SerialName("poster_path")
    val posterImagePath: String? = null,

    @SerialName("first_air_date")
    val releaseDate: String? = null,

    @SerialName("vote_average")
    val voteAverage: Float? = null,

    @SerialName("genre_ids")
    val genreIds: List<Int>? = null
 )