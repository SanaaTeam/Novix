package com.sanaa.identity.network

import com.sanaa.identity.network.postBody.CreateAccessTokenPostBody
import com.sanaa.identity.network.postBody.LoginPostBody
import com.sanaa.identity.network.response.CreateAccessTokenResponse
import com.sanaa.identity.network.response.CreateGuestSessionResponse
import com.sanaa.identity.network.response.CreateRequestTokenResponse
import com.sanaa.identity.network.response.LoginResponse
import com.sanaa.identity.network.response.RequestAccessTokenResponse
import com.sanaa.identity.network.response.ValidateKeyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AuthenticationApi {
    @GET("3/authentication/token/new")
    suspend fun createRequestToken(): Response<CreateRequestTokenResponse>

    @POST("3/authentication/token/validate_with_login")
    suspend fun login(
        @Body body: LoginPostBody,
    ): Response<LoginResponse>

    @GET("3/authentication/guest_session/new")
    suspend fun createGuestSession(): CreateGuestSessionResponse

    @POST("3/authentication")
    suspend fun validateKey(
        @HeaderMap header: Map<String, String>,
    ): ValidateKeyResponse

    @POST("4/auth/request_token")
    suspend fun requestUserAccessToken(): RequestAccessTokenResponse

    @POST("4/auth/access_token")
    suspend fun createUserAccessToken(
        @Body body: CreateAccessTokenPostBody,
    ): CreateAccessTokenResponse
}