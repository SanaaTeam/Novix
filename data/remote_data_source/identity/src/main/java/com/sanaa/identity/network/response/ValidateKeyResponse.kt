package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName

data class ValidateKeyResponse(
    @SerialName("success")
    val isSuccess: Boolean,

    @SerialName("status_code")
    val statusCode: Int?,
)