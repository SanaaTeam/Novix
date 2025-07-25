package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName


data class RequestAccessTokenResponse(
    @SerialName("success")
    val isSuccess: Boolean,

    @SerialName("status_code")
    val statusCode: Int?,

    @SerialName("status_message")
    val statusMessage: String?,

    @SerialName("request_token")
    val requestToken: String?,
)


