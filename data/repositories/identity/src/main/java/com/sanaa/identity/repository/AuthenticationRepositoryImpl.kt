package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.exceptions.InvalidUserOrPasswordException
import com.sanaa.identity.exceptions.ResponseException
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.LoginPostBody
import repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApiService,
    private val preferencesManager: PreferencesManager,
) : AuthenticationRepository {

    override suspend fun login(userName: String, password: String) {
        val requestToken = authenticationApi.createRequestToken().body()?.requestToken ?: ""
        val loginPostBody = LoginPostBody(
            username = userName,
            password = password,
            request_token = requestToken
        )
        val response = authenticationApi.login(loginPostBody)
        if (response.isSuccessful.not()) {
            throw InvalidUserOrPasswordException(userName)
        }
    }

    override suspend fun requestAccessToken(): String {
        val response = authenticationApi.requestUserAccessToken()
        if (response.isSuccessful.not()) {
            throw ResponseException(response.message())
        }

        val body = response.body()
        if (body != null && body.success && body.request_token != null) {
            return body.request_token!!
        } else {
            throw ResponseException(body?.status_message)
        }
    }

    override suspend fun createAccessToken() {
        val accessToken = requestAccessToken()
        preferencesManager.updateAuthorizationToken(accessToken)
    }

    override suspend fun createGuestSession() {
        authenticationApi.createGuestSession()
    }

    companion object {
        const val INVALID_USERNAME_OR_PASSWORD = 30
        const val EMAIL_NOT_VERIFIED_EXCEPTION = 32
    }
}