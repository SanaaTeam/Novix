package com.sanaa.movies.dataSource.dto

import kotlinx.serialization.SerialName


data class MovieDetailsDto(
    @SerialName("id")
    val id: Int,

    @SerialName("poster_path")
    val posterImagePath: String?,

    @SerialName("title")
    val title: String?,

    @SerialName("genre_ids")
    val genreIds: List<Int>?,

    @SerialName("vote_average")
    val voteAverage: Float?,

    @SerialName("release_date")
    val releaseDate: String?,

    @SerialName("overview")
    val overview: String?,

    @SerialName("runtime")
    val duration: Int?
)
