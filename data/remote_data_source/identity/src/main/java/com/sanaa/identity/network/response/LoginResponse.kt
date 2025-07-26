package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName

data class LoginResponse(
    @SerialName("success")
    val isSuccess: Boolean,

    @SerialName("status_code")
    val statusCode: Int?,

    @SerialName("status_message")
    val statusMessage: String?,

    @SerialName("expires_at")
    val expiresAt: String?,

    @SerialName("request_token")
    val requestToken: String?,
)