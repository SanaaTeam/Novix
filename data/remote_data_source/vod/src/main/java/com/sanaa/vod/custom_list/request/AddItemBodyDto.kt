package com.sanaa.vod.custom_list.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddItemBodyDto(
    @SerialName("media_id")  val mediaId: Int,
)

data class RemoveItemBodyDto(
    @SerialName("media_id")  val mediaId: Int,
)