package com.sanaa.vod.custom_list.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateListResponseDto(
    @SerialName("success") val success: Boolean,
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String,
    @SerialName("list_id") val listId: Int
)
