package com.sanaa.identity.dataSoruce.dataStore.mapper

import com.sanaa.identity.dataSoruce.local.dto.UserDto
import com.sanaa.identity.proto.User

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.name,
        username = this.username
    )
}