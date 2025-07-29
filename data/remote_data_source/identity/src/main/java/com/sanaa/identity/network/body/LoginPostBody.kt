package com.sanaa.identity.network.body

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class LoginPostBody(
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerialName("request_token")
    val requestToken: String,
)