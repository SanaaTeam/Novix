package com.sanaa.identity.dataSoruce.local.mapper

import com.sanaa.identity.dataSoruce.local.dto.UserDto
import entity.User

fun UserDto.toEntity(): User {
    return User(
        id = id,
        name = name,
        username = username
    )
}
