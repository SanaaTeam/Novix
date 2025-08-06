package com.sanaa.identity.dataSoruce.local.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("username")
    val username: String,
    @SerialName("profile-image-url")
    val profileImageUrl: String
)