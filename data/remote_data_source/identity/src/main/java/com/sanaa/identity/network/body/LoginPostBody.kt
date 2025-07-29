package com.sanaa.identity.network.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginPostBody(
    @SerialName("username")
    val username: String? = null,

    @SerialName("password")
    val password: String? = null,

    @SerialName("request_token")
    val requestToken: String? = null
)