package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestTokenResponse(
    @SerialName("success")
    val isSuccess: Boolean,

    @SerialName("status_code")
    val statusCode: Int?,

    @SerialName("expires_at")
    val expiresAt: String?,

    @SerialName("request_token")
    val requestToken: String?,
)