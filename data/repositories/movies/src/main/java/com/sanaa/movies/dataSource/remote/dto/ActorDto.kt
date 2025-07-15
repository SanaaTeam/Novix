package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String? = null,

    @SerialName("profile_path")
    val profileImagePath: String?= null,

    @SerialName("gender")
    val gender: Int? = null,

    @SerialName("character")
    val character: String?= null,

    @SerialName("biography")
    val biography: String? = null,

    )
