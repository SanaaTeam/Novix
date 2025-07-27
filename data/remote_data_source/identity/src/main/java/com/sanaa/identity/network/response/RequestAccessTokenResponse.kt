package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAccessTokenResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("status_code")
    val status_code: Int?,

    @SerialName("status_message")
    val status_message: String?,

    @SerialName("request_token")
    val request_token: String?,
)


