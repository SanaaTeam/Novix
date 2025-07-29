package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.util.wrapApiCall
import repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApiService,
    private val preferencesManager: PreferencesManager,
) : AuthenticationRepository {

    override suspend fun login(userName: String, password: String): Unit = wrapApiCall {
        val requestToken = getRequestTokenOrThrow()
        tryLoginOrThrow(userName, password, requestToken)
        val session = getSession(requestToken)
        saveSession(session)
    }

    private suspend fun getRequestTokenOrThrow(): String {
        val response = authenticationApi.createRequestToken()
        return response.requestToken ?: throw IllegalStateException("Request token is null")
    }

    private suspend fun tryLoginOrThrow(userName: String, password: String, requestToken: String) {
        val loginPostBody = LoginPostBody(
            username = userName,
            password = password,
            requestToken = requestToken
        )
        val response = authenticationApi.login(loginPostBody)
        if (!response.success) {
            throw IllegalStateException("Login failed")
        }
    }

    private suspend fun getSession(requestToken: String): CreateSessionResponse {
        val response = authenticationApi.createSession(mapOf("request_token" to requestToken))
        return response
    }

    private suspend fun saveSession(session: CreateSessionResponse) {
        preferencesManager.updateSessionId(session.sessionId)
        preferencesManager.setIsGuest(false)
    }

    override suspend fun createGuestSession(): Unit = wrapApiCall {
        val response = authenticationApi.createGuestSession()
        preferencesManager.updateSessionId(response.guest_session_id)
    }
}