package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable

data class MoviesByCategoryResponse(
    @SerialName("page") var page: Int? = null,
    @SerialName("results") var moviesByCategoryDto: ArrayList<MoviesByCategoryDto> = arrayListOf(),
    @SerialName("total_pages") var totalPages: Int? = null,
    @SerialName("total_results") var totalResults: Int? = null
) {
    @Serializable
    data class MoviesByCategoryDto(
        @SerialName("id") var id: Int,
        @SerialName("title") var title: String? = null,
        @SerialName("poster_path") var posterPath: String? = null,
        @SerialName("release_date") var releaseDate: String? = null,
        @SerialName("genre_ids") var genreIds: ArrayList<Int> = arrayListOf(),
        @SerialName("overview") var overview: String? = null,
        @SerialName("vote_average") var voteAverage: Double? = null,
    )
}