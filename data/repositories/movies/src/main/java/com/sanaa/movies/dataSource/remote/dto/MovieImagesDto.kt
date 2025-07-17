package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImagesDto(
    @SerialName("id") val id: Int,

    @SerialName("backdrops") val backdrops: List<ImageItemDto>,

    @SerialName("posters") val posters: List<ImageItemDto>,

    @SerialName("logos") val logos: List<ImageItemDto>
) {
    @Serializable
    data class ImageItemDto(
        @SerialName("aspect_ratio") val aspectRatio: Double? = null,
        @SerialName("height") val height: Int? = null,
        @SerialName("iso_639_1") val iso6391: String? = null,
        @SerialName("file_path") val filePath: String? = null,
        @SerialName("vote_average") val voteAverage: Double? = null,
        @SerialName("vote_count") val voteCount: Int? = null,
        @SerialName("width") val width: Int? = null
    )
}

