package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.util.exceptions.ResponseException
import com.sanaa.identity.util.wrapApiCall
import repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApiService,
    private val preferencesManager: PreferencesManager,
) : AuthenticationRepository {
    override suspend fun login(userName: String, password: String): Unit = wrapApiCall {
        getRequestTokenOrThrow().also { requestToken ->
            tryLoginOrThrow(userName, password, requestToken)
            getSession(requestToken).also { saveSession(it) }
        }
    }

    private suspend fun getRequestTokenOrThrow(): String {
        val response = authenticationApi.createRequestToken()
        if (response.isSuccessful.not()) {
            throw ResponseException(response.code())
        }
        return response.body()?.request_token ?: ""
    }

    private suspend fun tryLoginOrThrow(
        userName: String,
        password: String,
        requestToken: String,
    ) {
        val loginPostBody = LoginPostBody(
            username = userName,
            password = password,
            requestToken = requestToken
        )
        val response = authenticationApi.login(loginPostBody)
        if (response.isSuccessful.not())
            throw ResponseException(response.code())
    }

    private suspend fun getSession(requestToken: String): CreateSessionResponse {
        val response = authenticationApi.createSession(mapOf("request_token" to requestToken))
            .apply { if (isSuccessful.not()) throw ResponseException(code()) }
        return response.body() ?: throw ResponseException(response.code())
    }

    private suspend fun saveSession(it: CreateSessionResponse) {
        preferencesManager.updateSessionId(it.session_id)
        preferencesManager.setIsGuest(false)
    }

    override suspend fun createGuestSession(): Unit = wrapApiCall {
        val response = authenticationApi.createGuestSession()
            .apply { if (isSuccessful.not()) throw ResponseException(code()) }
        preferencesManager.updateSessionId(response.body()?.guest_session_id ?: "")
    }
}