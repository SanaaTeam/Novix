package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("key")
    val key: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("site")
    val site: String = "",
    @SerialName("type")
    val type: String = ""

)