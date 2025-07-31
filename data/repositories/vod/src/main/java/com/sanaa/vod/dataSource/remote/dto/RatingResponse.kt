package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName

data class RatingResponse(
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("status_message")
    val statusMessage: String
)
