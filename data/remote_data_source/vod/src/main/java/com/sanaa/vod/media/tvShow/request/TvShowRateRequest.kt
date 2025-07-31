package com.sanaa.vod.media.tvShow.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class TvShowRateRequest(
    @SerialName("value")
    val value: Float
)
