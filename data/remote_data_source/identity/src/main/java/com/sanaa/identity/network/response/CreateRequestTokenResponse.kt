package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("status_code")
    val status_code: Int?,

    @SerialName("expires_at")
    val expires_at: String?,

    @SerialName("request_token")
    val request_token: String?,
)