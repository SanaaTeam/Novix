package com.sanaa.series.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("character") val character: String = "",
    @SerialName("profile_path") val profilePath: String = "",
    @SerialName("gender") val gender: Int = 0,
)