package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponse(
    @SerialName("session_id")
    val session_id: String,

    @SerialName("success")
    val success: Boolean,
)