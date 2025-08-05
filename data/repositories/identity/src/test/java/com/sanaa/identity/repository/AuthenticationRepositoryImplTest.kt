package com.sanaa.identity.repository

import com.google.common.base.CharMatcher.any
import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.LocalUserDataSource
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.response.AccountDetailsResponse
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
    val userLocalDataSource = mockk<LocalUserDataSource>()
    lateinit var repository: AuthenticationRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = AuthenticationRepositoryImpl(
            api = apiService,
            preferences = preferencesManager,
            userLocalDataSource = userLocalDataSource
        )
    }

    @Test
    fun `login should call createRequestToken, login, createSession, save session and save user`() =
        runTest {
            val token = "test_token"
            val sessionId = "session_123"

            val accountResponse = AccountDetailsResponse(
                id = 100L,
                name = "Novix User",
                username = "novix"
            )

            coEvery { apiService.createRequestToken() } returns CreateRequestTokenResponse(
                success = true,
                requestToken = token
            )

            coEvery { apiService.login(any()) } returns LoginResponse(success = true)

            coEvery { apiService.createSession(mapOf("request_token" to token)) } returns CreateSessionResponse(
                success = true,
                sessionId = sessionId
            )

            coEvery { apiService.getAccountDetails(sessionId) } returns accountResponse

            coEvery { preferencesManager.updateSessionId(sessionId) } just runs
            coEvery { preferencesManager.setIsGuest(false) } just runs
            coEvery { userLocalDataSource.saveUser(any()) } just runs

            repository.login("Novix", "password")

            coVerify(exactly = 1) { preferencesManager.updateSessionId(sessionId) }
            coVerify(exactly = 1) { preferencesManager.setIsGuest(false) }
            coVerify(exactly = 1) {
                userLocalDataSource.saveUser(
                    withArg {
                        assertThat(it.id).isEqualTo(100L)
                        assertThat(it.username).isEqualTo("novix")
                        assertThat(it.name).isEqualTo("Novix User")
                    }
                )
            }
        }

    @Test
    fun `createGuestSession should call api and save guest session ID`() = runTest {
        val guestSessionId = "guest_123"
        coEvery { apiService.createGuestSession() } returns CreateGuestSessionResponse(
            success = true,
            expiresAt = null,
            guestSessionId = guestSessionId
        )
        coEvery { preferencesManager.updateSessionId(guestSessionId) } just runs

        repository.createGuestSession()

        coVerify(exactly = 1) { preferencesManager.updateSessionId(guestSessionId) }
    }

    @Test
    fun `logout should call delete user from datasource and clear from preferences manager`()=runTest {
        coEvery { userLocalDataSource.deleteUser() }  returns Unit
        coEvery { preferencesManager.clearSession() } returns Unit
        repository.logout()

        coVerify(exactly = 1) { userLocalDataSource.deleteUser() }
        coVerify(exactly = 1) { preferencesManager.clearSession() }
    }

}
