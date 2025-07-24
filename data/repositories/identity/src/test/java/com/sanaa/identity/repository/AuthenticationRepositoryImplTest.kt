package com.sanaa.identity.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.service.AuthenticationService
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.AuthenticationRepository

class AuthenticationRepositoryImplTest {
    private val authenticationService: AuthenticationService = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = AuthenticationRepositoryImpl(
            authenticationService, preferencesManager
        )
    }

    @Test
    fun `login() should login via AuthenticationService when try to login`() = runTest {
        // Given
        val userName = "Novix User"
        val password = "password"

        // When
        authenticationRepository.login(userName, password)

        // Then
        coVerify { authenticationService.login(userName, password) }
    }

    @Test
    fun `requestAccessToken() should request via AuthenticationService when try to request access token`() =
        runTest {
            // When
            authenticationRepository.requestAccessToken()

            // Then
            coVerify { authenticationService.requestUserAccessToken() }
        }


    @Test
    fun `createAccessToken() should create access token via AuthenticationService when try to create access token`() =
        runTest {
            // When
            authenticationRepository.createAccessToken()

            // Then
            coVerify { authenticationService.requestUserAccessToken() }
        }


    @Test
    fun `createAccessToken() should update the authorization token when try to create access token`() =
        runTest {
            // When
            authenticationRepository.createAccessToken()

            // Then
            coVerify { preferencesManager.updateAuthorizationToken(any()) }
        }

    @Test
    fun `createGuestSession() should create guest session via AuthenticationService when try to create guest session`() =
        runTest {
            // When
            authenticationRepository.createGuestSession()

            // Then
            coVerify { authenticationService.createGuestSession() }
        }
}