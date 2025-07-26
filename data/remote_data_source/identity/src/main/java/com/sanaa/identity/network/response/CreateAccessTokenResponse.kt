package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccessTokenResponse(
    @SerialName("success")
    val isSuccess: Boolean,

    @SerialName("status_code")
    val statusCode: Int?,

    @SerialName("access_token")
    val accessToken: String?,

    @SerialName("account_id")
    val accountId: String?,

    @SerialName("status_message")
    val statusMessage: String?,
)