package com.sanaa.identity.service

import com.google.common.truth.Truth
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.exceptions.InvalidTokenException
import com.sanaa.identity.network.AuthenticationApi
import com.sanaa.identity.network.response.CreateAccessTokenResponse
import com.sanaa.identity.network.response.RequestAccessTokenResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.Response

class AuthenticationServiceImplTest {
    private val authenticationApi: AuthenticationApi = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)
    private lateinit var authenticationService: AuthenticationService

    @BeforeEach
    fun setUp() {
        authenticationService = AuthenticationServiceImpl(authenticationApi, preferencesManager)
    }

    @Test
    fun `login() should create request token when try to login`() = runTest {
        // Given
        val userName = "Novix User"
        val password = "password"

        // When
        authenticationService.login(userName, password)

        // Then
        coVerify { authenticationApi.createRequestToken() }
    }

    @Test
    fun `login() should login via AuthenticationApi when try to login`() = runTest {
        // Given
        val userName = "Novix User"
        val password = "password"

        // When
        authenticationService.login(userName, password)

        // Then
        coVerify { authenticationApi.login(any()) }
    }

    @Test
    fun `requestUserAccessToken() should request creating token via AuthenticationApi when try to request create user access token`() =
        runTest {
            // Given
            val response = createRequestAccessTokenResponseResponse()
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

            // When
            authenticationService.requestUserAccessToken()

            // Then
            coVerify { authenticationApi.requestUserAccessToken() }
        }

    @Test
    fun `requestUserAccessToken() should return access token when response success`() = runTest {
        // Given
        val requestToken = "test request token"
        val response = createRequestAccessTokenResponseResponse(
            isSuccess = true, requestToken = requestToken
        )
        coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

        // When
        val result = authenticationService.requestUserAccessToken()

        // Then
        Truth.assertThat(result).isEqualTo(requestToken)
    }

    @Test
    fun `requestUserAccessToken() should throw InvalidTokenException when response failed`() =
        runTest {
            val response = createRequestAccessTokenResponseResponse(isSuccess = false)
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

            assertThrows<InvalidTokenException> {
                authenticationService.requestUserAccessToken()
            }
        }

    @Test
    fun `requestUserAccessToken() should throw InvalidTokenException when no request token returned`() =
        runTest {
            // Given
            val response = createRequestAccessTokenResponseResponse(
                isSuccess = true, requestToken = null
            )
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

            // When, Then
            assertThrows<InvalidTokenException> {
                authenticationService.requestUserAccessToken()
            }
        }


    @Test
    fun `createUserAccessToken() should create token via AuthenticationApi when try to create user access token`() =
        runTest {
            // Given
            val requestToken = "test request token"
            val response = getCreateAccessTokenResponseResponse(isSuccess = true)
            coEvery { authenticationApi.createUserAccessToken(any()) } returns response

            // When
            authenticationService.createUserAccessToken(requestToken)

            // Then
            coVerify { authenticationApi.createUserAccessToken(any()) }
        }


    @Test
    fun `createUserAccessToken() should throw InvalidTokenException when response failed`() =
        runTest {
            // Given
            val requestToken = "test request token"
            val response = createRequestAccessTokenResponseResponse(isSuccess = false)
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

            assertThrows<InvalidTokenException> {
                authenticationService.createUserAccessToken(requestToken)
            }
        }

    @Test
    fun `createUserAccessToken() should throw InvalidTokenException when no access token returned`() =
        runTest {
            // Given
            val requestToken = "test request token"
            val response = getCreateAccessTokenResponseResponse(
                isSuccess = true, accessToken = null
            )
            coEvery { authenticationApi.createUserAccessToken(any()) } returns response

            // When, Then
            assertThrows<InvalidTokenException> {
                authenticationService.createUserAccessToken(requestToken)
            }
        }

    @Test
    fun `createGuestSession()`() = runTest {
        // When
        authenticationService.createGuestSession()

        // Then
        coVerify {  authenticationApi.createGuestSession() }
    }

    private fun createRequestAccessTokenResponseResponse(
        isSuccess: Boolean = true,
        statusCode: Int? = null,
        requestToken: String? = "test request token",
        statusMessage: String? = null,
    ): RequestAccessTokenResponse {
        return RequestAccessTokenResponse(
            success = isSuccess,
            status_code = statusCode,
            status_message = statusMessage,
            request_token = requestToken
        )
    }

    private fun getCreateAccessTokenResponseResponse(
        isSuccess: Boolean = true,
        statusCode: Int? = null,
        statusMessage: String? = null,
        accessToken: String? = "test request token",
        accountId: String? = null,
    ): CreateAccessTokenResponse {
        return CreateAccessTokenResponse(
            isSuccess = isSuccess,
            statusCode = statusCode,
            accessToken = accessToken,
            statusMessage = statusMessage,
            accountId = accountId,
        )
    }
}