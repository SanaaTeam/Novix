package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.util.wrapApiCall
import repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val api: AuthenticationApiService,
    private val preferences: PreferencesManager
) : AuthenticationRepository {
    override suspend fun login(userName: String, password: String) = wrapApiCall {
        api.createRequestToken().requestToken?.let { token ->
            api.login(LoginPostBody(userName, password, token))
                .takeIf { it.success }
                ?.let {
                    val session = api.createSession(mapOf("request_token" to token))
                    saveSession(session)
                }
        }
        Unit
    }

    override suspend fun createGuestSession() = wrapApiCall {
        val response = api.createGuestSession()
        preferences.updateSessionId(response.guestSessionId)
    }

    private suspend fun saveSession(session: CreateSessionResponse) {
        preferences.updateSessionId(session.sessionId)
        preferences.setIsGuest(false)
    }

}