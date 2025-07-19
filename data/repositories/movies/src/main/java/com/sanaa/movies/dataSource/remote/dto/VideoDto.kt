package com.sanaa.movies.dataSource.remote.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class VideoDto(
    @SerialName("key") val key: String,
    @SerialName("site") val site: String,
    @SerialName("type") val type: String
)
