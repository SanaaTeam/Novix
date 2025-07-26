package com.sanaa.identity.service

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.exceptions.InvalidTokenException
import com.sanaa.identity.exceptions.InvalidUserOrPasswordException
import com.sanaa.identity.network.AuthenticationApi
import com.sanaa.identity.network.postBody.CreateAccessTokenPostBody
import com.sanaa.identity.network.postBody.LoginPostBody

class AuthenticationServiceImpl(
    private val authenticationApi: AuthenticationApi,
    private val preferencesManager: PreferencesManager,
) : AuthenticationService {
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

    override suspend fun requestUserAccessToken(): String {
        val response = authenticationApi.requestUserAccessToken()
        if (response.isSuccess && response.requestToken != null)
            return response.requestToken

        throw InvalidTokenException(message = response.statusMessage)
    }

    override suspend fun createUserAccessToken(requestToken: String) {
        val postBody = CreateAccessTokenPostBody(requestToken)
        val response = authenticationApi.createUserAccessToken(postBody)
        if (response.isSuccess && response.accessToken != null) {
            preferencesManager.updateAuthorizationToken(response.accessToken)
        } else {
            throw InvalidTokenException(response.statusMessage)
        }
    }

    override suspend fun createGuestSession() {
        authenticationApi.createGuestSession()
    }

    companion object {
        const val INVALID_USERNAME_OR_PASSWORD = 30
        const val EMAIL_NOT_VERIFIED_EXCEPTION = 32
    }
}