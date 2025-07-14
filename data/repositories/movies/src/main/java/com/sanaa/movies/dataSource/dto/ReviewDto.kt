package com.sanaa.movies.dataSource.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ReviewDto(
    @SerialName("author")
    val author: String,

    @SerialName("author_details")
    val authorDetails: AuthorDetailsDto,

    @SerialName("content")
    val content: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("url")
    val url: String
)

@Serializable
data class AuthorDetailsDto(
    @SerialName("name")
    val name: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("avatar_path")
    val avatarPath: String? = null,

    @SerialName("rating")
    val rating: Float? = null
)