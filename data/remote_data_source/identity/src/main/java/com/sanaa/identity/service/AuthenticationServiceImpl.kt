package com.sanaa.identity.service

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.exceptions.InvalidTokenException
import com.sanaa.identity.network.AuthenticationApi
import com.sanaa.identity.network.postBody.CreateAccessTokenPostBody
import com.sanaa.identity.network.postBody.LoginPostBody

class AuthenticationServiceImpl(
    private val authenticationApi: AuthenticationApi,
    private val preferencesManager: PreferencesManager,
) : AuthenticationService {
    override suspend fun login(userName: String, password: String) {
        val requestToken = authenticationApi.createRequestToken()
        val loginPostBody = LoginPostBody(
            userName = userName,
            password = password,
            requestToken = requestToken.requestToken
        )
        authenticationApi.login(loginPostBody)
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
        if (response.isSuccess && response.accessToken != null)
            preferencesManager.updateAuthorizationToken(response.accessToken)

        throw InvalidTokenException(response.statusMessage)
    }

    override suspend fun createGuestSession() {
        authenticationApi.createGuestSession()
    }
}