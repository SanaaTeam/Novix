package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.dataSoruce.local.dto.UserDto
import com.sanaa.identity.dataSoruce.local.mapper.toEntity
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.util.wrapApiCall
import entity.User
import exceptions.NoLoggedInUserException
import repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val api: AuthenticationApiService,
    private val userLocalDataSource: LocalUserDataSource,
    private val preferences: PreferencesManager
) : AuthenticationRepository {
    override suspend fun login(userName: String, password: String) = wrapApiCall {
        api.createRequestToken().requestToken?.let { token ->
            api.login(LoginPostBody(userName, password, token)).takeIf { it.success }?.let {
                val session = api.createSession(mapOf("request_token" to token))
                saveSession(session)

                val account = api.getAccountDetails(
                    session.sessionId
                )
                userLocalDataSource.saveUser(
                    UserDto(
                        id = account.id,
                        name = account.name.orEmpty(),
                        username = account.username.orEmpty()
                    )
                )
            }
        }
        Unit
    }

    override suspend fun createGuestSession() = wrapApiCall {
        val response = api.createGuestSession()
        preferences.updateSessionId(response.guestSessionId)
    }

    override suspend fun getLoggedUser(): User {
        return userLocalDataSource.getLoggedUser()
            ?.toEntity() ?: throw NoLoggedInUserException()
    }

    override suspend fun isLoggedIn(): Boolean = wrapApiCall {
        return userLocalDataSource.getLoggedUser() != null
    }

    override suspend fun logout() {
        userLocalDataSource.deleteUser()
        preferences.clearSession()
    }

    private suspend fun saveSession(session: CreateSessionResponse) {
        preferences.updateSessionId(session.sessionId)
        preferences.setIsGuest(false)
    }
}