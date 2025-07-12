package com.sanaa.search.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchDto(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String?,

    @SerialName("poster_path")
    val posterImagePath: String?,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("vote_average")
    val voteAverage: Float?,

    @SerialName("genre_ids")
    val genreIds: List<Int>?
)