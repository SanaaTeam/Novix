package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGuestSessionResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("status_code")
    val status_code: Int?,

    @SerialName("expires_at")
    val expires_at: String?,

    @SerialName("guest_session_id")
    val guest_session_id: String,
)


