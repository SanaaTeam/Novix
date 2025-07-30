package com.sanaa.identity.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("expires_at") val expiresAt: String? = null,
    @SerialName("request_token") val requestToken: String? = null,
    @SerialName("status_message") val statusMessage: String? = null,
    @SerialName("status_code") val statusCode: Int? = null
)

@Serializable
data class AccountDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("include_adult") val includeAdult: Boolean? = null,
)