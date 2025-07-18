package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    @SerialName("id")
    val id: Int,
    @SerialName("poster_path")
    val posterImagePath: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("vote_average")
    val voteAverage: Float? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("runtime")
    val duration: Int? = null,
    @SerialName("adult")
    var adult: Boolean? = null,
    @SerialName("backdrop_path")
    var backdropPath: String? = null,
    @SerialName("budget")
    var budget: Int? = null,
    @SerialName("genres")
    var genres: ArrayList<Genres> = arrayListOf(),
    @SerialName("homepage")
    var homepage: String? = null,
    @SerialName("imdb_id")
    var imdbId: String? = null,
    @SerialName("origin_country")
    var originCountry: ArrayList<String> = arrayListOf(),
    @SerialName("original_language")
    var originalLanguage: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    @SerialName("popularity")
    var popularity: Double? = null,
    @SerialName("revenue")
    var revenue: Int? = null,
    @SerialName("status")
    var status: String? = null,
    @SerialName("tagline")
    var tagline: String? = null,
    @SerialName("video")
    var video: Boolean? = null,
    @SerialName("vote_count")
    var voteCount: Int? = null
) {
    @Serializable
    data class Genres(

        @SerialName("id")
        var id: Int? = null,
        @SerialName("name")
        var name: String? = null

    )
}


