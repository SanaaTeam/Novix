package com.sanaa.vod.dataSource.remote.dto.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("author")
    val author: String? = null,
    @SerialName("author_details")
    val authorDetails: AuthorDetailsDto = AuthorDetailsDto(),
    @SerialName("content")
    val content: String = "",
    @SerialName("created_at")
    val createdAt: String = ""
)