package com.sanaa.vod.custom_list.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateListBodyDto(
    val name: String,
    val description: String,
    @SerialName("iso_639_1") val language: String
)