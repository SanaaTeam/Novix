package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenResponse(
    @SerialName("success")
    val success: Boolean = false,

    @SerialName("status_code")
    val statusCode: Int? = null,

    @SerialName("expires_at")
    val expiresAt: String? = null,

    @SerialName("request_token")
    val requestToken: String? = null
)