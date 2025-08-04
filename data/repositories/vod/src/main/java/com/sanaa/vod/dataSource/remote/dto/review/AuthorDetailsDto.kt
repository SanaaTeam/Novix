package com.sanaa.vod.dataSource.remote.dto.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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