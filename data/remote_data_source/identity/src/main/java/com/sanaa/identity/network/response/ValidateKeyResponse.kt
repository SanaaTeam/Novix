package com.sanaa.identity.network.response

import kotlinx.serialization.SerialName

data class ValidateKeyResponse(
    @SerialName("success")
    val success: Boolean?,
)