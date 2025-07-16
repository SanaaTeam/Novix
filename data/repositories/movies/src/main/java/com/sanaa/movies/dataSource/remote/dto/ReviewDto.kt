package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    @SerialName("id") var id: Int,
    @SerialName("page") var page: Int? = null,
    @SerialName("results") var results: ArrayList<Results> = arrayListOf(),
    @SerialName("total_pages") var totalPages: Int? = null,
    @SerialName("total_results") var totalResults: Int? = null
) {
    @Serializable
    data class Results(
        @SerialName("author") var author: String? = null,
        @SerialName("author_details") var authorDetails: AuthorDetailsDto? = AuthorDetailsDto(),
        @SerialName("content") var content: String? = null,
        @SerialName("created_at") var createdAt: String? = null,
        @SerialName("id") var id: String,
        @SerialName("updated_at") var updatedAt: String? = null,
        @SerialName("url") var url: String? = null

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
}

