package com.sanaa.search.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowSearchDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String?,

    @SerialName("poster_path")
    val posterImagePath: String?,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("vote_average")
    val voteAverage: Double?,

    @SerialName("genre_ids")
    val genreIds: List<Int>?
 )