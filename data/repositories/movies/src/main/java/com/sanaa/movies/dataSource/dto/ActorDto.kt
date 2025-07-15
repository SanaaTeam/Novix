package com.sanaa.movies.dataSource.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String?,

    @SerialName("profile_path")
    val profileImagePath: String?,

    @SerialName("gender")
    val gender: Int,

    @SerialName("character")
    val character: String?,

    @SerialName("biography")
    val biography: String

    )
