package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName

data class LoginResponse(
    @SerialName("success")
    val success: Boolean?,

    @SerialName("expires_at")
    val expiresAt: String,

    @SerialName("request_token")
    val requestToken: String,
)