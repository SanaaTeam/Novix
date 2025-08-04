package com.sanaa.vod.dataSource.remote.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorSearchDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profile_path")
    val profileImagePath: String? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    @SerialName("gender")
    val gender: Int? = null,
    )