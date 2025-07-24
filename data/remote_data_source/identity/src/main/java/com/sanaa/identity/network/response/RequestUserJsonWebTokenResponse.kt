package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName


data class RequestUserJsonWebTokenResponse(
    @SerialName("success")
    val isSuccess: Boolean,

    @SerialName("status_code")
    val statusCode: Boolean,

    @SerialName("status_message")
    val statusMessage: String,

    @SerialName("request_token")
    val requestToken: String,
)


