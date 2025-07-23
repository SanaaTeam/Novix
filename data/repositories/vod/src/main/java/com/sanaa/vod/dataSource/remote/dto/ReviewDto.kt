package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ReviewDto(
    @SerialName("id") val id: String = "",

    @SerialName("author") val author: String? = null,

    @SerialName("author_details") val authorDetails: AuthorDetailsDto = AuthorDetailsDto(),

    @SerialName("content") val content: String = "",

    @SerialName("created_at") val createdAt: String = ""
)

@Serializable
data class AuthorDetailsDto(
    @SerialName("name") val name: String? = null,

    @SerialName("username") val username: String? = null,

    @SerialName("avatar_path") val avatarPath: String? = null,

    @SerialName("rating") val rating: Float? = null
)