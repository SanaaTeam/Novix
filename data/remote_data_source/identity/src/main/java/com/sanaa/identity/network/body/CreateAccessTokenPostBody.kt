package com.sanaa.identity.network.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAccessTokenPostBody(
    @SerialName("request_token")
    val request_token: String,
)