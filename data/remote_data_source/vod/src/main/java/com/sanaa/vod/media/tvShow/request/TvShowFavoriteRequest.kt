package com.sanaa.vod.media.tvShow.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class TvShowFavoriteRequest(
    @SerialName("media_type")
    val mediaType: String = "tv",
    @SerialName("media_id")
    val mediaId: Int,
    @SerialName("favorite")
    val favorite: Boolean,
)
