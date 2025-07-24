package com.sanaa.identity.network.postBody

import kotlinx.serialization.SerialName

data class LoginPostBody(
    @SerialName("username")
    val userName: String,

    @SerialName("password")
    val password: String,

    @SerialName("request_token")
    val requestToken: String,
)