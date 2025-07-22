package com.sanaa.vod.media.actor.response

import com.sanaa.vod.dataSource.remote.dto.ImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorImagesResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("profiles")
    val profiles: List<ImageDto>
)