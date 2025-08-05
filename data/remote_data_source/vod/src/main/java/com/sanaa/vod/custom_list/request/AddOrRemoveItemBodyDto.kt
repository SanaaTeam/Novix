package com.sanaa.vod.custom_list.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddOrRemoveItemBodyDto(
    @SerialName("media_id")  val mediaId: Int,
)