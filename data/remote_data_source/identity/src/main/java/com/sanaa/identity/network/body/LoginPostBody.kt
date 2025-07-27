package com.sanaa.identity.network.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginPostBody(
    @SerialName("username")
    val username: String,

    @SerialName("password")
    val password: String,

    @SerialName("request_token")
    val request_token: String,
)