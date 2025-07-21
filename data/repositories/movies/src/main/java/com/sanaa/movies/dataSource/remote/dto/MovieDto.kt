package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("id") val id: Int,
    @SerialName("poster_path") val posterImagePath: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("genres") val genres: List<GenreDto>? = null,
    @SerialName("vote_average") val voteAverage: Float? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("runtime") val duration: Int? = null,
) {
    @Serializable
    data class GenreDto(
        @SerialName("id") var id: Int? = null, @SerialName("name") var name: String? = null

    )
}

@Serializable
data class MovieImagesDto(
    @SerialName("file_path") val filePath: String,
)

@Serializable
data class MovieVideoDto(
    @SerialName("key") val key: String,
    @SerialName("site") val site: String,
    @SerialName("type") val type: String
)


