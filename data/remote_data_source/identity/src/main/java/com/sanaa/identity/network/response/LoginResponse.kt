package com.sanaa.identity.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("status_code")
    val status_code: Int?,

    @SerializedName("status_message")
    val status_message: String?,

    @SerializedName("expires_at")
    val expires_at: String?,

    @SerializedName("request_token")
    val requestToken: String?,
)