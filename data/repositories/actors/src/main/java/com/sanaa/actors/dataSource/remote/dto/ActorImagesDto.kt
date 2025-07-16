package com.sanaa.actors.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorImagesDto(
    @SerialName("id") val id: Int,

    @SerialName("profiles") val profiles: List<ProfileImageDto>
) {
    @Serializable
    data class ProfileImageDto(
        @SerialName("aspect_ratio") val aspectRatio: Double,

        @SerialName("height") val imageHeight: Int,

        @SerialName("width") val imageWidth: Int,

        @SerialName("iso_639_1") val languageCode: String? = null,

        @SerialName("file_path") val path: String,

        @SerialName("vote_average") val averageVote: Double,

        @SerialName("vote_count") val voteCount: Int
    )
}