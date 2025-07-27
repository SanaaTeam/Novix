package com.sanaa.identity.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.identity.exceptions.InvalidUserOrPasswordException
import com.sanaa.identity.exceptions.ResponseException
import com.sanaa.identity.network.AuthenticationApiService
import com.sanaa.identity.network.response.CreateAccessTokenResponse
import com.sanaa.identity.network.response.CreateRequestTokenResponse
import com.sanaa.identity.network.response.LoginResponse
import com.sanaa.identity.network.response.RequestAccessTokenResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.AuthenticationRepository
import retrofit2.Response

class AuthenticationRepositoryImplTest {
    private val authenticationApi: AuthenticationApiService = mockk(relaxed = true)
    private val preferencesManager: PreferencesManager = mockk(relaxed = true)

    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = AuthenticationRepositoryImpl(authenticationApi, preferencesManager)
    }

    @Test
    fun `login() should complete successfully when api calls are successful`() = runTest {
        // Given
        val userName = "Novix User"
        val password = "password"
        val fakeToken = "test_request_token"
        coEvery { authenticationApi.createRequestToken() } returns Response.success(createRequestTokenResponse(requestToken = fakeToken))
        coEvery { authenticationApi.login(any()) } returns Response.success(createLoginResponse(requestToken = fakeToken))

        // When
        authenticationRepository.login(userName, password)

        // Then
        coVerify(exactly = 1) { authenticationApi.createRequestToken() }
        coVerify(exactly = 1) { authenticationApi.login(any()) }
    }

    @Test
    fun `login() should throw InvalidUserOrPasswordException when login api call is not successful`() = runTest {
        // Given
        val userName = "Novix User"
        val password = "wrong_password"
        coEvery { authenticationApi.createRequestToken() } returns Response.success(createRequestTokenResponse())
        // Simulate a failed login (e.g., 401 Unauthorized)
        coEvery { authenticationApi.login(any()) } returns Response.error(401, "".toResponseBody(null))

        // When, Then
        assertThrows<InvalidUserOrPasswordException> {
            authenticationRepository.login(userName, password)
        }
    }

    @Test
    fun `requestAccessToken() should return token when api response is successful and body is valid`() = runTest {
        // Given
        val expectedToken = "test_request_token"
        val response = createRequestUserAccessTokenResponse(isSuccess = true, requestToken = expectedToken)
        coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

        // When
        val result = authenticationRepository.requestAccessToken()

        // Then
        assertThat(result).isEqualTo(expectedToken)
        coVerify(exactly = 1) { authenticationApi.requestUserAccessToken() }
    }

    @Test
    fun `requestAccessToken() should throw ResponseException when api response is not successful`() = runTest {
        // Given
        coEvery { authenticationApi.requestUserAccessToken() } returns Response.error(500, "".toResponseBody(null))

        // When, Then
        assertThrows<ResponseException> {
            authenticationRepository.requestAccessToken()
        }
    }

    @Test
    fun `requestAccessToken() should throw ResponseException when response body success is false`() =
        runTest {
            // Given
            val response = createRequestUserAccessTokenResponse(isSuccess = false)
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

            // When, Then
            assertThrows<ResponseException> {
                authenticationRepository.requestAccessToken()
            }
        }

    @Test
    fun `requestAccessToken() should throw ResponseException when response body has no request token`() =
        runTest {
            // Given
            val response = createRequestUserAccessTokenResponse(isSuccess = true, requestToken = null)
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)

            // When, Then
            assertThrows<ResponseException> {
                authenticationRepository.requestAccessToken()
            }
        }

    @Test
    fun `createAccessToken() should request an access token and then update preferences`() =
        runTest {
            // Given
            val fakeToken = "fake_request_token_from_api"
            val response = createRequestUserAccessTokenResponse(isSuccess = true, requestToken = fakeToken)
            coEvery { authenticationApi.requestUserAccessToken() } returns Response.success(response)
            coEvery { preferencesManager.updateAuthorizationToken(any()) } just runs

            // When
            authenticationRepository.createAccessToken()

            // Then
            // Verify it first requests the token
            coVerify(exactly = 1) { authenticationApi.requestUserAccessToken() }
            // Then verify it saves the *exact same token* to preferences
            coVerify(exactly = 1) { preferencesManager.updateAuthorizationToken(fakeToken) }
        }

    @Test
    fun `createGuestSession() should call createGuestSession on the api`() = runTest {
        // When
        authenticationRepository.createGuestSession()

        // Then
        coVerify(exactly = 1) { authenticationApi.createGuestSession() }
    }

    private fun createRequestTokenResponse(
        isSuccess: Boolean = true,
        statusCode: Int? = null,
        expiresAt: String? = null,
        requestToken: String? = "test_request_token"
    ): CreateRequestTokenResponse {
        return CreateRequestTokenResponse(
            isSuccess = isSuccess,
            statusCode = statusCode,
            expiresAt = expiresAt,
            requestToken = requestToken
        )
    }

    private fun createLoginResponse(
        isSuccess: Boolean = true,
        statusCode: Int? = null,
        statusMessage: String? = null,
        expiresAt: String? = null,
        requestToken: String? = "test_request_token"
    ): LoginResponse {
        return LoginResponse(
            isSuccess = isSuccess,
            statusCode = statusCode,
            statusMessage = statusMessage,
            expiresAt = expiresAt,
            requestToken = requestToken
        )
    }

    private fun createRequestUserAccessTokenResponse(
        isSuccess: Boolean = true,
        statusCode: Int? = null,
        requestToken: String? = "test_request_token",
        statusMessage: String? = null,
    ): RequestAccessTokenResponse {
        return RequestAccessTokenResponse(
            success = isSuccess,
            status_code = statusCode,
            status_message = statusMessage,
            request_token = requestToken
        )
    }

    private fun getCreateAccessTokenResponse(
        isSuccess: Boolean = true,
        statusCode: Int? = null,
        statusMessage: String? = null,
        accessToken: String? = "test_access_token",
        accountId: String? = null,
    ): CreateAccessTokenResponse {
        return CreateAccessTokenResponse(
            isSuccess = isSuccess,
            statusCode = statusCode,
            statusMessage = statusMessage,
            accessToken = accessToken,
            accountId = accountId,
        )
    }
}
