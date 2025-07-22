package com.sanaa.vod.media.actor.response

import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorActorCastCreditResponse(
    @SerialName("id") val actorId: Int,
    @SerialName("cast") val cast: List<ActorCastCreditDto>
)