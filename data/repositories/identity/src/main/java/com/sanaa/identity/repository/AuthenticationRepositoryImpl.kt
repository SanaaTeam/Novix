package com.sanaa.identity.repository

import android.util.Log
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.body.LoginPostBody
import com.sanaa.identity.util.exceptions.ResponseException
import com.sanaa.identity.util.wrapApiCall
import repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationApi: AuthenticationApiService,
    private val preferencesManager: PreferencesManager,
) : AuthenticationRepository {

    override suspend fun login(userName: String, password: String): Unit = wrapApiCall {
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

    override suspend fun requestAccessToken(): String = wrapApiCall {
        val response = authenticationApi.requestUserAccessToken()
        if (response.isSuccessful.not()) {
            throw ResponseException(response.code())
        }

        val body = response.body()
        if (body != null && body.success && body.request_token != null) {
            return body.request_token!!
        } else {
            throw ResponseException(response.code())
        }
    }

    override suspend fun createAccessToken(requestToken: String) = wrapApiCall {
        val accessToken = requestAccessToken()
        preferencesManager.updateSessionId(accessToken)
    }

    override suspend fun createGuestSession(): Unit = wrapApiCall {
        val response = authenticationApi.createGuestSession()
        if (response.isSuccess && response.guestSessionId != null) {
            preferencesManager.updateSessionId(response.guestSessionId!!)
            Log.d("GuestSession", "Guest Session ID: ${response.guestSessionId}")
        } else {
            throw ResponseException(response.statusCode ?: -1)
        }
    }

}