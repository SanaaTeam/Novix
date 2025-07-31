package com.sanaa.vod.media.movie.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieRateRequest(
    @SerialName("value")
    val value: Float
)
