package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.service.AuthenticationService
import repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService,
    private val preferencesManager: PreferencesManager,
) : AuthenticationRepository {
    override suspend fun login(userName: String, password: String) {
        authenticationService.login(userName, password)
    }

    override suspend fun requestAccessToken(): String {
        return authenticationService.requestUserAccessToken()
    }

    override suspend fun createAccessToken(requestToken: String) {
        val accessToken = authenticationService.createUserAccessToken(requestToken)
        preferencesManager.updateAuthorizationToken(accessToken)
    }

    override suspend fun createGuestSession() {
        authenticationService.createGuestSession()
    }
}