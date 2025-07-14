package com.sanaa.movies.dataSource.dto

import entity.Actor.Gender
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ActorDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String?,

    @SerialName("profile_path")
    val profileImagePath: String?,

    @SerialName("gender")
    val gender: Gender,

    @SerialName("character")
    val character: String?,

    @SerialName("biography")
    val biography: String

    )
