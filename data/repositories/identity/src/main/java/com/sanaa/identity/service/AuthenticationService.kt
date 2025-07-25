package com.sanaa.identity.service

interface AuthenticationService {
    suspend fun login(userName: String, password: String)
    suspend fun requestUserAccessToken(): String
    suspend fun createUserAccessToken(requestToken: String)
    suspend fun createGuestSession()
}