package com.sanaa.vod.custom_list.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateListBodyDto(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("language") val language: String
)