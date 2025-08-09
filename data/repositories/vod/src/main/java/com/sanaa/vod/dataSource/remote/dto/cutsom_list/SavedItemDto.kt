package com.sanaa.vod.dataSource.remote.dto.cutsom_list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedItemDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("adult") val adult: Boolean? = null,
    @SerialName("video") val video: Boolean? = null,
    @SerialName("vote_average") val voteAverage: Float? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("popularity") val popularity: Float? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null
)
