package com.sanaa.identity.dataSoruce.dataStore

import androidx.datastore.core.DataStore
import com.sanaa.identity.dataSoruce.dataStore.mapper.toDto
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dto.UserDto
import com.sanaa.identity.proto.User
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<User>
) : LocalUserDataSource {

    override suspend fun getLoggedUser(): UserDto? {
        return dataStore.data
            .map { user ->
                if (user == User.getDefaultInstance()) null else user.toDto()
            }
            .firstOrNull()
    }

    override suspend fun saveUser(user: UserDto) {
        dataStore.updateData { current ->
            current.toBuilder()
                .setId(user.id)
                .setUsername(user.username)
                .setName(user.name)
                .setProfileImageUrl(user.profileImageUrl)
                .build()
        }
    }

    override suspend fun deleteUser() {
        dataStore.updateData {
            User.getDefaultInstance()
        }
    }
}