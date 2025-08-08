package com.sanaa.vod.custom_list.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemStatusResponseDto(
    @SerialName("id") val id: String,
    @SerialName("item_present") val itemPresent: Boolean
)