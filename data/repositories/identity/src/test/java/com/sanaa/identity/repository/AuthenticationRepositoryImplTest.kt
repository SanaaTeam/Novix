package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.response.CreateGuestSessionResponse
import com.sanaa.identity.network.response.CreateRequestTokenResponse
import com.sanaa.identity.network.response.CreateSessionResponse
import com.sanaa.identity.network.response.LoginResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationRepositoryImplTest {
    val apiService = mockk<AuthenticationApiService>()
    val preferencesManager = mockk<PreferencesManager>()
    lateinit var repository: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = AuthenticationRepositoryImpl(
            api = apiService,
            preferences = preferencesManager
        )
    }

    @Test
    fun `login should call createRequestToken, login, createSession and save session`() = runTest {
        val token = "test_token"
        val sessionId = "session_123"

        coEvery { apiService.createRequestToken() } returns CreateRequestTokenResponse(
            success = true,
            requestToken = token
        )

        coEvery { apiService.login(any()) } returns LoginResponse(success = true)

        coEvery { apiService.createSession(mapOf("request_token" to token)) } returns CreateSessionResponse(
            success = true,
            sessionId = sessionId
        )

        coEvery { preferencesManager.updateSessionId(sessionId) } just runs
        coEvery { preferencesManager.setIsGuest(false) } just runs

        repository.login("Novix", "password")

        coVerify(exactly = 1) { preferencesManager.updateSessionId(sessionId) }
    }

    @Test
    fun `createGuestSession should call api and save guest session ID`() = runTest {
        val guestSessionId = "guest_123"
        coEvery { apiService.createGuestSession() } returns CreateGuestSessionResponse(
            success = true,
            statusCode = null,
            expiresAt = null,
            guestSessionId = guestSessionId
        )
        coEvery { preferencesManager.updateSessionId(guestSessionId) } just runs

        repository.createGuestSession()

        coVerify(exactly = 1) { preferencesManager.updateSessionId(guestSessionId) }
    }
}
