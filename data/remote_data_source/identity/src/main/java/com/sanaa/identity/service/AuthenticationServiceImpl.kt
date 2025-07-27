package com.sanaa.identity.service

import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.CreateAccessTokenPostBody
import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.util.exceptions.InvalidTokenException
import com.sanaa.identity.util.exceptions.ResponseException
import com.sanaa.identity.util.wrapApiCall

class AuthenticationServiceImpl(
    private val authenticationApi: AuthenticationApiService,
) : AuthenticationService {
    override suspend fun login(userName: String, password: String) = wrapApiCall {
        val requestToken = authenticationApi.createRequestToken().body()?.requestToken ?: ""
        val loginPostBody = LoginPostBody(
            username = userName,
            password = password,
            request_token = requestToken
        )
        val response = authenticationApi.login(loginPostBody)
        if (response.isSuccessful.not()) {
            throw ResponseException(response.code())
        }
    }

    override suspend fun requestUserAccessToken(): String = wrapApiCall {
        val response = authenticationApi.requestUserAccessToken()
        if (response.isSuccessful.not()) {
            throw ResponseException(response.code())
        }

        val body = response.body()
        if (body != null && body.success && body.request_token != null)
            return body.request_token
        else
            throw ResponseException(response.code())
    }

    override suspend fun createUserAccessToken(requestToken: String): String = wrapApiCall {
        val postBody = CreateAccessTokenPostBody(requestToken)
        val response = authenticationApi.createUserAccessToken(postBody)
        if (response.isSuccess && response.accessToken != null) {
            response.accessToken
        } else {
            throw InvalidTokenException(response.statusMessage)
        }
    }

    override suspend fun createGuestSession(): Unit = wrapApiCall {
        authenticationApi.createGuestSession()
    }

}