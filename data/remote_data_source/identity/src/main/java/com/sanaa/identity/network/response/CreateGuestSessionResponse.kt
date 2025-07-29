package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGuestSessionResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("status_code")
    val statusCode: Int?,

    @SerialName("expires_at")
    val expiresAt: String?,

    @SerialName("guest_session_id")
    val guestSessionId: String,
)


