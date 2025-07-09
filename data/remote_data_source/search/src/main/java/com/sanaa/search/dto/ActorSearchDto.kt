package com.sanaa.search.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ActorSearchDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val profilePath: String
)
