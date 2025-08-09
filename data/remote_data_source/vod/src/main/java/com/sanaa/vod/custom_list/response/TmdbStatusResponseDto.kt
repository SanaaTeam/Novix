package com.sanaa.vod.custom_list.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbStatusResponseDto(
    @SerialName("success") val success: Boolean,
    @SerialName("status_code")  val statusCode: Int? = null,
    @SerialName("status_message") val statusMessage: String? = null
)