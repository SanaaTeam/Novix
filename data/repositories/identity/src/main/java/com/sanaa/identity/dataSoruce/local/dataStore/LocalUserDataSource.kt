package com.sanaa.identity.dataSoruce.local.dataStore

import com.sanaa.identity.dataSoruce.local.dto.UserDto

interface LocalUserDataSource {
    suspend fun getLoggedUser(): UserDto?

    suspend fun saveUser(user: UserDto)

    suspend fun deleteUser()
}