package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistActionDto(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val status_code: Int,
    @SerialName("status_message")
    val status_message: String
)

