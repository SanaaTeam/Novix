package com.sanaa.identity.dataSoruce.local.dataStore

import com.sanaa.identity.dataSoruce.local.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {
    fun getLoggedUser(): Flow<UserDto?>

    suspend fun saveUser(user: UserDto)

    suspend fun deleteUser()
}