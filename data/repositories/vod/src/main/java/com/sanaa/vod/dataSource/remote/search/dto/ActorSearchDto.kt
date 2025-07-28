package com.sanaa.vod.dataSource.remote.search.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorSearchDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String?,
    @SerialName("profile_path") val profileImagePath: String?,
    @SerialName("known_for_department") val knownForDepartment: String?,
    @SerialName("gender") val gender: Int?,

    )