package com.sanaa.vod.dataSource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchlistRequestBody(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    @SerialName("watchlist") val watchlist: Boolean
)