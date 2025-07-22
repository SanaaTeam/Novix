package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null
)