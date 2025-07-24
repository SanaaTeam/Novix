package com.sanaa.identity.network.postBody

import kotlinx.serialization.SerialName

data class CreateAccessTokenPostBody(
    @SerialName("request_token")
    val requestToken: String,
)